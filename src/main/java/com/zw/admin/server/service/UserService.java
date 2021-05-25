package com.zw.admin.server.service;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.ResponseDto;
import com.zw.admin.server.dto.UserDto;
import com.zw.admin.server.model.R;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import org.apache.commons.lang3.StringUtils;

public interface UserService {

	ResultVO<?> register(UserDto userDto);

	ResultVO<?> saveUser(UserDto userDto);

	ResultVO<?> updateUser1(UserDto userDto);

	ResultVO<?> updateUser(UserDto userDto);

	String passwordEncoder(String credentials, String salt);

	String passwordEncoderNew(String credentials);

	/**
	 * web忘记密码修改
	 *
	 * @param phone
	 * @param email
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	ResultVO<?> changePasswordWeb(String phone, String email, String oldPassword, String newPassword,String id);

	/**
	 * api通过id修改
	 *
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	ResultVO<?> changePassword(String id, String newPassword);

	ResultVO<?> getUserList(Map<String, Object> params, Integer page, Integer num);

	ResultVO<?> getRecentLogin();

	/**
	 * 判断是否临时号
	 *
	 * @param user
	 * @return true是 false不是
	 */
	boolean checkIsTemporary(UserDto user);

	/**
	 * 获取带游戏id字符串的用户数据
	 * 
	 * @return
	 */
	User getUserHaveGame();

	ResultVO<?> syncUser(User user);

	ResultVO<?> saveUserRpc(UserDto userDto);

	ResultVO<?> registerUserRpc(UserDto userDto);

	ResultVO<?> updateUserRpc(UserDto userDto);

	ResultVO<?> syncUserRpc(UserDto user);

	User getUserByIdRpc(String id);

	Boolean deleteUserRpc(String id);

	ResultVO<?> updateUser1Rpc(UserDto userDto);

	/**
	 * api通过id修改
	 *
	 * @param id
	 * @param newPassword
	 * @return
	 */
	//ResultVO<?> changePasswordRpc(String id, String newPassword);

	int initUser();

	public Boolean initUserRpc(String phone, String email, String password);


	/**
	 * 功能描述:验证码校验
	 *
	 *
	 * @param userDto
	 * @return java.lang.Boolean
	 * @author larry
	 * @Date 2020/11/6 11:54
	 */
	public Boolean checkVerificationCode(UserDto userDto);

	public ResultVO<?> getOnlineUser(UserDto user);

	/**
	 * 功能描述:添加在线用户接口
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/6 16:50
	 */
	public ResultVO<?> updateOnlineUser(UserDto user);

	/**
	 * 功能描述:离线用户转在线
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/12/7 16:38
	 */
	public ResultVO<?> offlineTurnOnline(UserDto userDto);

	/**
	 * 功能描述:根据患者id生成一个激活url地址
	 *
	 * @param userId
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/12/9 17:42
	 */
	public ResultVO<?> createActiveUrl(String userId);

	public Boolean save(User user);

	/**
	 * 合并用户，如果用户ID已经存在则更新用户信息
	 * @param user user
	 * @return bool
	 */
	public Boolean merge(User user);

	public Boolean update(UserDto user);

	/**
	 * 功能描述:修改邮箱或者手机号
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/15 18:01
	 */
	public ResultVO<?> updateEmailOrPhone(UserDto user);

	/**
	 * 功能描述:登录转在线
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/23 11:32
	 */
	public ResultVO<?> loginOrTurnByWxApplet(UserDto user);

	/**
	 * 功能描述:根据token获取当前登录的用户数据
	 *
	 * @param loginToken
	 * @return com.zw.admin.server.model.User
	 * @author larry
	 * @Date 2021/5/22 17:44
	 */
	public User getUserByToken(String loginToken);
}
