package com.zw.admin.server.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.dao.XiaoShouYiDao;
import com.zw.admin.server.dto.XoqlDto;
import com.zw.admin.server.model.XiaoShouYi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author larry
 * @Description:销售易API
 * @date 2020/11/20 12:02
 */
@Slf4j
@Component
public class XiaoShouYiApi {

    //连接器id
    private String client_id = "1a500e962bc198e4942a27f10cfaf2fc";

    //连接器密码
    private String client_secret = "9258b05eaacd2f80db3f8d3ede6fb00d";

    //回调地址
    private String redirect_uri = "https://crm.xiaoshouyi.com";

    //销售易账号
    private String username = "software@fftai.com";

    //销售易密码
    private String password = "flyfftai2015";

    //销售易安全令牌
    private String securityToke = "ulveZ2Zn";

    private String getTokenUrl = "https://api.xiaoshouyi.com/oauth2/token";

    //超时时间50ms
    private int timeOut = 50000;

    //xoql请求地址
    private String xoqlUrl = "https://api.xiaoshouyi.com/rest/data/v2.0/query/xoql";

    @Resource
    private XiaoShouYiDao xiaoShouYiDao;

    /**
     * 功能描述:授权码模式获取accessToken
     *
     * @param
     * @return java.lang.String
     * @author larry
     * @Date 2020/11/20 13:44
     */
    public String getAccessTokenByPassword() {
        XiaoShouYi xiaoShouYi = xiaoShouYiDao.getOne();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("client_id", xiaoShouYi.getClientId());
        params.put("client_secret", xiaoShouYi.getClientSecret());
        params.put("grant_type", "password");
        params.put("redirect_uri", xiaoShouYi.getRedirectUri());
        params.put("username", xiaoShouYi.getUsername());
        //密码时真实密码加安全令牌
        params.put("password", xiaoShouYi.getPassword() + xiaoShouYi.getSecurityToken());
        String accessToken = HttpUtil.post(getTokenUrl, params);
        return accessToken;
    }

    /**
     * 功能描述:根据传入的xoql语句，查询销售易中信息
     *
     * @param sql
     * @return java.lang.String
     * @author larry
     * @Date 2020/11/20 16:11
     */
    public String getMsgByXoql(String sql, Boolean useSimpleCode,String accessToken) {
        Map<String, Object> params = new HashMap<String, Object>();
        //请求头中认证信息
        //String token = "Bearer " + this.getAccessTokenByPassword();
        params.put("xoql", sql);
        params.put("useSimpleCode", useSimpleCode);
        log.info("xiao_shou_yi_api,请求:"+JSONObject.toJSONString(params));
        String result = HttpRequest.post(xoqlUrl).header("Authorization", accessToken).
                form(params).
                timeout(timeOut).execute().body();
        //XoqlDto xoqlDto = (XoqlDto) JSONObject.parseObject(result, XoqlDto.class);
        log.info("xiao_shou_yi_api,响应:"+result);
        return result;
    }
}
