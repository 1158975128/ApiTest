package com.zw.admin.server.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dao.UserDao;
import com.zw.admin.server.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.zw.admin.server.config.CustomToken;
import com.zw.admin.server.dto.Token;
import com.zw.admin.server.dto.UserDto;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import com.zw.admin.server.service.TokenManager;
import com.zw.admin.server.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 登陆相关接口
 *
 * @author THF
 */
@Slf4j
@Api(tags = "登陆")
@RestController
@RequestMapping
public class LoginController {

	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private ServerProperties serverProperties;
	@Autowired
	private UserService userService;
	@Resource
	private RedisTemplate<String,String> redisTemplate;
	//线程池
	@Resource
	TaskExecutor taskExecutor;
	@Resource
	private UserDao userDao;
	private Long expire = 60l;
	private String pre = "v4:";
	@Value("${sessionTimeOut}")
	private Long sessionTimeOut;

	String vueLogin = "http://192.168.3.49:8080";

	// @LogAnnotation
	@ApiOperation(value = "web登陆")
	@PostMapping("/sys/login")
	public ResultVO<?> login(String phone, String email, String password, Boolean rememberMe,String openid) {
		if(StringUtils.isBlank(phone)&&StringUtils.isBlank(email)){
			return R.error(1,"手机或邮箱为空");
		}
		//如果远程有数据，拉到本地，角色为医师
		Boolean aBoolean = userService.initUserRpc(phone, email, password);
		if(!aBoolean){
			return R.error(1, "用户不存在");
		}
		String sha1 = DigestUtils.sha1Hex(password);
		String encryptByPublicKey = null;
		Token token = null;
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		try {
			encryptByPublicKey = RSAUtil.encryptByPublicKey(sha1);
			password = encryptByPublicKey;
			CustomToken customToken = new CustomToken(null, password, rememberMe, phone, email, null, null, null,openid);
			SecurityUtils.getSubject().login(customToken);

			// 设置shiro的session过期时间
			/*SecurityUtils.getSubject().getSession()
					.setTimeout(serverProperties.getServlet().getSession().getTimeout().toMillis());*/
			SecurityUtils.getSubject().getSession().setTimeout(sessionTimeOut);
			User user = UserUtil.getCurrentUser();
			customToken.setUserId(user.getId());
			token = tokenManager.saveToken(customToken);
			log.info("token---" + token.getToken());
			map.put("token", token);
			map.put("user", user);
		} catch (Exception e) {
			return R.error(1, e.getMessage());
		}
		return R.success(map);
	}

	// @LogAnnotation
	@ApiOperation(value = "Restful登陆")
	@PostMapping("/sys/login/restful")
	public ResultVO<?> restfulLogin(String username, String phone, String email, String password, String isRemember,
			String isTemporary, String machineId) {
		//如果远程有数据，拉到本地，角色为医师
		Boolean aBoolean = userService.initUserRpc(phone, email, password);
		if(!aBoolean){
			return R.error(1, "用户不存在");
		}
		Token token = null;
		CustomToken customToken = new CustomToken(username, password, false, phone, email, isRemember, isTemporary,
				machineId,null);
		try {
			SecurityUtils.getSubject().login(customToken);
			// 设置shiro的session过期时间
			/*SecurityUtils.getSubject().getSession()
					.setTimeout(serverProperties.getServlet().getSession().getTimeout().toMillis());*/
			//重新设置失效时间
			SecurityUtils.getSubject().getSession().setTimeout(sessionTimeOut);
			token = tokenManager.saveToken(customToken);
		} catch (Exception e) {
			return R.error(1, e.getMessage());
		}
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		User user = userService.getUserHaveGame();
		map.put("token", token);
		map.put("user", user);
		return R.success(map);
	}

	@ApiOperation(value = "当前登录用户")
	@GetMapping("/sys/login")
	public User getLoginInfo() {
		return UserUtil.getCurrentUser();
	}

	@ApiOperation(value = "用户注销")
	@PostMapping("/sys/logout")
	public ResultVO<?> logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout(); //
			return R.success();
		}
		return R.error(1, "未登录");
	}

	@LogAnnotation(module = "web注册用户")
	@PostMapping("/register")
	@ApiOperation(value = "web注册用户")
	// @RequiresPermissions("sys:user:add")
	public ResultVO<?> register(UserDto userDto) {
		//判断验证码是否正确，不正确直接返回
		Boolean aBoolean = userService.checkVerificationCode(userDto);
		if(!aBoolean){
			return R.error(1,"验证码错误!");
		}
		return userService.registerUserRpc(userDto);
		// return userDto;
	}

	/**
	 * 功能描述:检验用户是否存在
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/8/12 11:17
	 */
	public ResultVO<?> checkUser(UserDto userDto) {

		return R.success();
	}

	/**
	 * 
	 * @param type    发送类型 1邮箱 2手机
	 * @param address 发送地址
	 * @return
	 */
	// @LogAnnotation
	@PostMapping(value = "/api/verificationCode")
	@ApiOperation(value = "发送验证码")
	public ResultVO<?> verificationCode(int type, String address) {
		int code = (int) ((Math.random() * 9 + 1) * 100000);
		if (type == 1) {
			EmailUtil.sendMail(address, type, code);
		} else {
			try {
				SendSmsResponse sendSmsResponse = SmsUtil.sendSms(address, code);
				log.info("短信发送:code:"+sendSmsResponse.getCode()+",message:"+sendSmsResponse.getMessage());
			} catch (ClientException e) {
				e.printStackTrace();
				return R.error(1, "短信发送错误");
			}
		}
		return R.success(code);
	}

	/**
	 * 功能描述:开发者注册时验证码发送
	 *
	 * @param type
	 * @param address
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/8/11 15:36
	 */
	@PostMapping(value = "/api/sendVerificationCode")
	@ApiOperation(value = "发送验证码")
	public ResultVO<?> sendVerificationCode(int type, String address) {
		//异步发送
		taskExecutor.execute(()->this.send(type,address));
		return R.success();
	}

	public void send(int type, String address){
		int code = (int) ((Math.random() * 9 + 1) * 100000);
		if (type == 1) {
			EmailUtil.sendMail(address, type, code);
		} else {
			try {
				SendSmsResponse sendSmsResponse = SmsUtil.sendSms(address, code);
				log.info("短信发送:code:"+sendSmsResponse.getCode()+",message:"+sendSmsResponse.getMessage());
			} catch (ClientException e) {
				e.printStackTrace();
				log.info(address+":短信发送错误");
				//return R.error(1, "短信发送错误");
			}
		}
		String verificationCode = ""+code;
		String key = pre+address;
		//存入redis
		redisTemplate.opsForValue().set(key,verificationCode,expire, TimeUnit.SECONDS);
		log.info("address:"+address+",验证码:"+verificationCode);
	}

	@LogAnnotation(module = "忘记密码")
	@PostMapping("/forget")
	@ApiOperation(value = "忘记密码")
	// @RequiresPermissions("sys:user:password")
	public ResultVO<?> changePassword(String phone, String email, String oldPassword, String newPassword,String id) {
		return userService.changePasswordWeb(phone, email, oldPassword, newPassword,id);
	}

	// @LogAnnotation
	@PostMapping("/getRecentLogin")
	@ApiOperation(value = "获取最近3条登录用户")
	public ResultVO<?> getRecentLogin() {
		return userService.getRecentLogin();
	}

	// @LogAnnotation
	@PostMapping("/redirectWeb")
	@ApiOperation(value = "验证token并跳转网站")
	public ResultVO<?> handleServiceOrder(String t, HttpServletResponse resp) {

		UsernamePasswordToken token = tokenManager.getToken(t);
		try {
			if (token != null) {
				resp.sendRedirect(vueLogin + "/#/login?t=" + t);
			} else {
				return R.error(1, "token错误");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return R.error(1, e.getMessage());
		}
		return R.success();
	}

	/**
	 * 功能描述:生成加密后密码
	 *
	 * @param password
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/12/15 17:03
	 */
	@PostMapping("/getPassword")
	@ApiOperation(value = "生成加密后密码")
	public ResultVO<?> getPassword(String password) {
		String newPassword = DigestUtils.sha1Hex(password);
		return R.success(newPassword);
	}

	/**
	 * 功能描述:生成加密后密码
	 *
	 * @param password
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/12/15 17:03
	 */
	@GetMapping("/notAuthenticated")
	@ApiOperation(value = "生成加密后密码")
	public ResultVO<?> notAuthenticated(String password) {
		throw new UnknownAccountException("请登录");
	}



}
