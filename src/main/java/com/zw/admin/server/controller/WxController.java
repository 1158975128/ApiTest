package com.zw.admin.server.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dao.UserDao;
import com.zw.admin.server.dto.DecodePhoneDto;
import com.zw.admin.server.dto.UserDto;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import com.zw.admin.server.utils.FileUtil;
import com.zw.admin.server.utils.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author larry
 * @Description:微信小程序服务
 * @date 2021/2/4 10:59
 */
@Slf4j
@RestController
@RequestMapping("/wx")
public class WxController {

    @Value("${AppID}")
    private String appId;
    @Value("${AppSecret}")
    private String appSecret;

    @Value("${updateUserUrl}")
    private String updateUserUrl;

    @Value("${qrCodePath}")
    private String qrCodePath;

    @Value("${wxOpenidSessionTimeOut}")
    private Long wxOpenidSessionTimeOut;

    private String getWxSession = "https://api.weixin.qq.com/sns/jscode2session";
    //获取accessToken
    private String getAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    //获取二维码
    private String getQrCode = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode";

    @Resource
    private UserDao userDao;
    @Resource
    LoginController loginController;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 功能描述:获取会话缓存
     *
     * @param jsCode
     * @return com.zw.admin.server.model.ResultVO
     * @author larry
     * @Date 2021/2/4 17:10
     */
/*    @LogAnnotation(module = "微信小程序获取会话缓存")
    @PostMapping("/getSessionByCode")
    public ResultVO getSessionByCode(@RequestBody DecodePhoneDto decodePhoneDto) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", decodePhoneDto.getJsCode());
        params.put("grant_type", "authorization_code");
        String result = HttpUtil.get(getWxSession, params);
        JSONObject jsonObject = new JSONObject(result);
        String openid = jsonObject.getStr("openid");
        log.info("小程序会话信息:"+jsonObject);
        return R.success(jsonObject);
    }*/

    /**
     * 功能描述:获取会话缓存
     *
     * @param decodePhoneDto
     * @return com.zw.admin.server.model.ResultVO
     * @author larry
     * @Date 2021/2/4 17:10
     */
    @LogAnnotation(module = "微信小程序获取会话缓存")
    @PostMapping("/getSessionByCode")
    public ResultVO getSessionByCode(@RequestBody DecodePhoneDto decodePhoneDto) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", decodePhoneDto.getJsCode());
        params.put("grant_type", "authorization_code");
        String result = HttpUtil.get(getWxSession, params);
        JSONObject jsonObject = new JSONObject(result);
        String openid = jsonObject.getStr("openid");
        String sessionKey = jsonObject.getStr("session_key");
        log.info("小程序会话信息:"+jsonObject);
        String sessionId = UUID.randomUUID().toString();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        redisTemplate.opsForValue().set(sessionId,openid,wxOpenidSessionTimeOut, TimeUnit.SECONDS);
        if (StringUtils.isBlank(openid)) {
            return R.error(1, "获取会话信息错误");
        } else {
            redisTemplate.opsForValue().set(sessionId,openid,wxOpenidSessionTimeOut, TimeUnit.SECONDS);
            //拿到openid后去数据库查询，如果是数据库的openid已经存在，直接登录，不存在去绑定
            User byOpenid = userDao.getByOpenid(openid);
            resultMap.put("sessionKey",sessionKey);
            resultMap.put("sessionId",sessionId);
            if(byOpenid !=null){
                //可以直接登录
                resultMap.put("operate","1");
                return R.success(resultMap);
            }else {
                //需要转在线
                resultMap.put("operate","2");
                return R.success(resultMap);
            }
        }
    }

    /**
     * 功能描述:获取会话缓存
     *
     * @param decodePhoneDto
     * @return com.zw.admin.server.model.ResultVO
     * @author larry
     * @Date 2021/2/4 17:10
     */
    @LogAnnotation(module = "微信绑定后直接登录")
    @PostMapping("/loginByOpenId")
    public ResultVO loginByOpenId(@RequestBody DecodePhoneDto decodePhoneDto) {
        String sessionKey = decodePhoneDto.getSessionKey();
        String openid = redisTemplate.opsForValue().get(sessionKey);
        if (StringUtils.isBlank(openid)) {
            return R.error(1, "登录失败");
        } else {
            //拿到openid后去数据库查询，如果是数据库的openid已经存在，直接登录，不存在去绑定
            User byOpenid = userDao.getByOpenid(openid);
            if (byOpenid != null) {
                //可以直接登录
                //resultMap.put("1",)
                return loginController.login(byOpenid.getPhone(), byOpenid.getEmail(), byOpenid.getPassword(), Boolean.FALSE, openid);
            }
            return R.error(1, "登录失败");
        }
    }

    /**
     * 功能描述:根据密文解密获取用户手机号
     *
     * @param decodePhoneDto
     * @return com.zw.admin.server.model.ResultVO
     * @author larry
     * @Date 2021/2/4 16:32
     */
    @LogAnnotation(module = "微信小程序解析手机号")
    @PostMapping("/decodePhone")
    public ResultVO decodePhone(@RequestBody DecodePhoneDto decodePhoneDto) {
        byte[] dataByte = Base64.decode(decodePhoneDto.getEncryptedData());
        byte[] keyByte = Base64.decode(decodePhoneDto.getSessionKey());
        byte[] ivByte = Base64.decode(decodePhoneDto.getIv());
        try {
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                log.info("手机号解析:"+result);
                return R.success(new JSONObject(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error(1, "解析失败");
    }

    /**
     * 功能描述:根据appid，appSecret获取accessToken
     *
     * @param appId
     * @param appSecret
     * @return java.lang.String
     * @author larry
     * @Date 2021/2/25 17:21
     */
    public String accessToken(String appId, String appSecret) {
        String grant_type = "client_credential";
        HashMap<String, Object> params = new HashMap<>();
        params.put("grant_type",grant_type);
        params.put("appid",appId);
        params.put("secret",appSecret);
        String result = HttpUtil.get(getAccessTokenUrl, params);
        JSONObject jsonObject = new JSONObject(result);
        return (String)jsonObject.get("access_token");
    }

    @LogAnnotation(module = "微信小程序解析手机号")
    @PostMapping("/getQrCode")
    public ResultVO getQrCode(@RequestBody Map<String,Object> param) throws Exception{
        Object width = param.get("width");
        Object path = param.get("path");
        String access_token= accessToken(appId,appSecret);
        log.info("access_token:"+access_token);
        HashMap<String, Object> params = new HashMap<>();
        params.put("access_token",access_token);
        params.put("path",path);
        params.put("width",width);
        JSONObject jsonObject = new JSONObject(param);
        String para = jsonObject.toString();
        String getQrCodeUrl = getQrCode+"?access_token="+access_token;
        String post = HttpUtil.post(getQrCodeUrl, para);
        byte[] bytes = post.getBytes("utf-8");
        String string = java.util.Base64.getEncoder().encodeToString(bytes);
        log.info(post);
        return R.success(string);
    }

    /**
     * 功能描述:读取微信地址
     *
     * @param param
     * @return com.zw.admin.server.model.ResultVO
     * @author larry
     * @Date 2021/2/26 9:41
     */
    @LogAnnotation(module = "微信小程序解析手机号")
    @PostMapping("/qrImage")
    public ResultVO qrImage(@RequestBody Map<String, Object> param) throws Exception {
        String  width = String.valueOf(param.get("width"));
        String  path = (String) param.get("path");
        path = path+"?id=123456";
        try {
            String string = this.getQrCodeBase64(width,path);
            return R.success(string);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(1,"二维码生成错误");
        }
        
    }

    /**
     * 功能描述:unity生成小程序二维码base64字符串
     *
     * @param user
     * @return com.zw.admin.server.model.ResultVO
     * @author larry
     * @Date 2021/3/12 17:30
     */
    @LogAnnotation(module = "微信小程序二维码")
    @PostMapping("/qrImageStr")
    public ResultVO qrImageStr(User user) throws Exception {
        String id = user.getId();
        if (StringUtils.isBlank(id)){
            return R.error(1,"缺少用户id");
        }
        //合理性校验
        User byId = userDao.getById(id);
        if(byId==null){
            return R.error(1,"查找不到患者");
        }
        String width = "400";
        String path = qrCodePath + "?id=" + user.getId();
        try {
            String string = this.getQrCodeBase64(width, path);
            return R.success(string);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(1, "二维码生成错误");
        }
    }

    public String getQrCodeBase64(String width,String path) throws Exception{
        
        String access_token = accessToken(appId, appSecret);
        log.info("access_token:" + access_token);
        HashMap<String, Object> params = new HashMap<>();
        //params.put("access_token", access_token);
        params.put("path", path);
        params.put("width", width);
        JSONObject jsonObject = new JSONObject(params);
        String para = jsonObject.toString();
        String getQrCodeUrl = getQrCode + "?access_token=" + access_token;

        URL url = new URL(getQrCodeUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        //post协议必须带上
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(5000);
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(para.getBytes());
        outputStream.flush();
        outputStream.close();
        InputStream inputStream = conn.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        byte[] picByte = byteArrayOutputStream.toByteArray();

        inputStream.close();
        //读取流文件转换成base64
        String string = java.util.Base64.getEncoder().encodeToString(picByte);
        return string;
    }




}
