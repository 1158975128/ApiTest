package com.zw.admin.server.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zw.admin.server.activemq.producer.Provider;
import com.zw.admin.server.config.CustomToken;
import com.zw.admin.server.dto.UserPlaceDto;
import com.zw.admin.server.service.TokenManager;
import com.zw.admin.server.service.UserPlaceRelService;
import com.zw.admin.server.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.zw.admin.server.constants.UserConstants;
import com.zw.admin.server.dao.GameDao;
import com.zw.admin.server.dao.UserDao;
import com.zw.admin.server.dto.UserDto;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import com.zw.admin.server.model.User.Status;
import com.zw.admin.server.model.UserGame;
import com.zw.admin.server.service.UserService;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private GameDao gameDao;

	@Autowired
	private RestTemplate restTemplate;

	@Resource
	private Provider provider;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private RedisTemplate<String,String> redisTemplate;
	@Resource
	private UserPlaceRelService userPlaceRelService;

	//更新用户信息URL
	@Value("${updateUserUrl}")
	private String updateUserUrl;

	//根据电话和邮箱查询用户是否已经存在
	@Value("${getUserByPhoneOrEmailUrl}")
	private String getUserByPhoneOrEmailUrl;

	//注册用户
	@Value("${saveUserUrl}")
	private String saveUserUrl;

	//分页展示
	@Value("${userListUrl}")
	private String userListUrl;

	//根据id查询用户信息
	@Value("${getUserByIdUrl}")
	private String getUserByIdUrl;

	//根据ids删除用户
	@Value("${deleteUserByIdUrl}")
	private String deleteUserByIdsUrl;

	//根据ids删除用户
	@Value("${getRecentLoginUrl}")
	private String getRecentLoginUrl;

	//查询离线用户是否已经存在
	@Value("${getByTemporary}")
	private String getByTemporary;

	//查询离线用户是否已经存在
	@Value("${selectByEmailPhoneAndId}")
	private String selectByEmailPhoneAndId;

	@Value("${offlineToOnline}")
	private String offlineToOnline;

	public static final Long PATIENT = 0L;// 病人

	public static final Long ICU = 1L;// 治疗师

	public static final Long ADMIN = 2L;// 医院管理员

	public static final Long DEVELOPER = 3L;// 游戏开发者

	public static final Long ROOT = 4L;// 超级管理员

	public static final Long USER = 5L;// 普通用户

	private int unChecked = 0;

	private int checked = 1;

	private Integer email = 1;

	private Integer phone = 2;
	//前缀
	private String pre = "v4:";
	//激活码前缀
	private String active_code = "active_code:";
	//id映射存活时间
	private Integer active_code_time = 3;
	//三天内的转在线最大人数
	private int activeMaxNum = 1000000;
	//患者id存入redis前缀
	private String userIdPre = "userId:";
	@Resource
	private TokenManager tokenManager;

	@Override
	@Transactional
	public ResultVO<?> register(UserDto userDto) {
		ResultVO<?> resultVO = checkUser(userDto);
		// 1重复 2参数错误
		if (resultVO.getCode() == 1 || resultVO.getCode() == 2) {
			resultVO.setCode(1);
			return resultVO;
		}

		String sha1 = DigestUtils.sha1Hex(userDto.getPassword());
		User user = userDto;
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");

		User activeUser = UserUtil.getCurrentUser();

		Long identity = 0l;
		// 帮别人注册
		if (activeUser != null) {
			String userId = activeUser.getId();
			identity = activeUser.getIdentity();
			if (identity == ICU) {
				identity = PATIENT;
				user.setIdentity(PATIENT);
				user.setParentId(userId);
			} else if (identity == ADMIN) {
				identity = ICU;
				user.setIdentity(ICU);
				user.setParentId(userId);
			}
		} else {
			// 自己注册
			identity = user.getIdentity();
		}
		List<Long> list = new ArrayList<>();
		list.add(identity);
		saveUserRoles(uuid, list);

		user.setId(uuid);
		user.setPassword(sha1);
		int i = userDao.save(user);
		if (i < 1) {
			return R.error(1, "注册失败");
		}

		// 添加re默认游戏
		saveUserGame(userDto);

		return R.success();
	}

	@Override
	@Transactional
	public ResultVO<?> saveUser(UserDto userDto) {
		ResultVO<?> resultVO = checkUser(userDto);
		if (resultVO.getCode() == 1 || resultVO.getCode() == 2) {
			resultVO.setCode(1);
			return resultVO;
		}
		User user = userDto;
		String uuid = "";

		if (StringUtils.isBlank(user.getId())) {
			uuid = UUID.randomUUID().toString().replaceAll("-", "");
		} else {
			uuid = user.getId();
		}
		user.setId(uuid);
		user.setPassword(passwordEncoderNew(user.getPassword()));
		userDao.save(user);
		// 添加用户身份
		List<Long> list = new ArrayList<>();
		list.add(user.getIdentity());
		saveUserRoles(uuid, list);
		// 添加默认游戏
		saveUserGame(userDto);
		Map<String, String> map = new HashMap<>();
		map.put("id", uuid);
		return R.success(map);
	}

	// 添加新用户游戏关系
	private void saveUserGame(User userDto) {
		List<UserGame> byIsDefault = gameDao.getByIsDefault();
		for (UserGame userGame : byIsDefault) {
			userGame.setUserId(userDto.getId());
			userGame.setUsername(userDto.getUsername());
			userGame.setIsBuy(1L);
			//gameDao.saveUG(userGame);
		}
		//批量操作
		if(!CollectionUtils.isEmpty(byIsDefault)){
			gameDao.saveUGBatch(byIsDefault);
		}
	}

	/**
	 * 清除旧的用户角色，绑定新角色
	 * @param userId userId
	 * @param roleIds roleIds
	 */
	private void saveUserRoles(String userId, List<Long> roleIds) {
		if (!CollectionUtils.isEmpty(roleIds)) {
			userDao.deleteUserRole(userId);
			userDao.saveUserRoles(userId, roleIds);
		}
	}

	@Override
	public String passwordEncoder(String credentials, String salt) {
		Object object = new SimpleHash("MD5", credentials, salt, UserConstants.HASH_ITERATIONS);
		return object.toString();
	}

	@Override
	public String passwordEncoderNew(String credentials) {
		String decryptByPrivateKey = null;
		try {
			decryptByPrivateKey = RSAUtil.decryptByPrivateKey(credentials);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptByPrivateKey;
	}

	@Override
	public ResultVO<?> changePasswordWeb(String phone, String email, String oldPassword, String newPassword,String id) {
		User u = userDao.getById(id);
		if (u == null) {
			return R.error(1, "用户不存在");
		}
		if (StringUtils.isNotBlank(oldPassword)) {
			if (!u.getPassword().equals(DigestUtils.sha1Hex(oldPassword))) {
				return R.error(1, "原密码错误");
			}
		}
		String sha1 = DigestUtils.sha1Hex(newPassword);
		userDao.changePassword(u.getId(), sha1);
		//发送修改用户消息,同步用户数据到其他系统start
		//判断是否为在线用户，如果是离线用户本系统修改，如果是在线用户全局修改
		User user = new User();
		user.setId(id);
		if(StringUtils.isBlank(id)){
			user.setId(u.getId());
		}
		user.setPassword(sha1);
//		//发送同步消息队列
		String userStr = JSON.toJSONStringWithDateFormat(user, DateUtil.DATE_TIME_PATTERN);
		provider.sendTopic(userStr,"update");
		//this.sendUserStr(user);
		//发送修改用户密码end
		return R.success();
	}

	@Override
	public ResultVO<?> changePassword(String id, String newPassword) {

		if (StringUtils.isAllBlank(id, newPassword)) {
			return R.error(1, "参数错误");
		}
		passwordEncoderNew(newPassword);

		userDao.changePassword(id, passwordEncoderNew(newPassword));
		//发送修改用户消息,同步用户数据到其他系统start
		User user = new User();
		user.setId(id);
		user.setPassword(passwordEncoderNew(newPassword));
		//发送同步消息队列
		String userStr = JSON.toJSONStringWithDateFormat(user, DateUtil.DATE_TIME_PATTERN);
		provider.sendTopic(userStr,"update");
		//发送修改用户密码end
		return R.success();
	}

	@Override
	public ResultVO<?> updateUser1(UserDto userDto) {
		log.info("updateUser1="+userDto.getIdentity());

		String userId = userDto.getId();
		if (userId == null) {
			return R.error(1, "参数错误");
		}

		//判断用户是否可以修改，如果用户中的邮箱或者手机号数据库中已经存在，且id和该用户不一致，不允许修改start
		String email = userDto.getEmail();
		String phone = userDto.getPhone();
		//如果手机号或者邮箱不为空
//		if (StringUtils.isNotBlank(email) || StringUtils.isNotBlank(phone)) {
//			List<User> list = userDao.getUserByEmailPhoneAndId(userDto);
//			if (!CollectionUtils.isEmpty(list)) {
//				return R.error(1, "手机号或者邮箱已经存在");
//			}
//		}
		//如果是非离线
		if (!"1".equals(userDto.getIsTemporary())) {
			userDto.setPhone(null);
			userDto.setEmail(null);
		}
		Long identity = userDto.getIdentity();
		String isTemporary = userDto.getIsTemporary();
		User user1 = userDao.getById(userId);
		if(user1==null){
			return R.error(1, "用户不存在");
		}
		log.info("ICU==identity="+(ICU==identity));
		log.info("identity"+identity);
		//离线医生改成在线
		if("0".equals(isTemporary)&&(identity==1)){
			if(StringUtils.isBlank(email) && StringUtils.isBlank(phone)){
				user1.setEmail(user1.getEmail());
				user1.setPhone(user1.getPhone());
			}
			//如果是用户为在线且为医生，就去判断用户服务是否存在id不同且邮箱或者手机号相同的用户，如果存在，直接返回不可以修改
			user1.setIsChecked(1);
			//设置修改用户为已验证
			userDto.setIsChecked(1);
			//远程查询
			String result = restTemplate.postForObject(selectByEmailPhoneAndId, user1, String.class);
			if(StringUtils.isNotBlank(result)){
				return R.error(1, "该用户无法转成在线用户!");
			}
		}
		//判断用户是否可以修改，如果用户中的邮箱或者手机号数据库中已经存在，且id和该用户不一致，不允许修改end
		String status = userDto.getStatus();

		userDao.update(userDto);
		if (status != null) {
			if (Status.DISABLED.equals(status)) {
				userDao.deleteUserDev(userId);
			} else if (Status.LOCKED.equals(status)) {
				userDao.saveUserRole(userId, DEVELOPER);
			}
		}
		//发送消息给其他服务，同步修改数据start
		String userStr = JSON.toJSONStringWithDateFormat(userDto, DateUtil.DATE_TIME_PATTERN);
		provider.sendTopic(userStr,"update");
		//发送消息给其他服务，同步修改数据end
		return R.success();
	}

	@Override
	@Transactional
	public ResultVO<?> updateUser(UserDto userDto) {

		// userDto.setIdentity(userDto.getRoleIds().get(0));
		userDao.update(userDto);
		if (userDto.getIdentity() != null) {
			List<Long> list = new ArrayList<>();
			list.add(userDto.getIdentity());
			saveUserRoles(userDto.getId(), list);
		}
		updateUserSession(userDto.getId());
		return R.success(userDto);
	}

	private void updateUserSession(String id) {
		User current = UserUtil.getCurrentUser();
		if (current.getId().equals(id)) {
			User user = userDao.getById(id);
			UserUtil.setUserSession(user);
		}
	}

	@Override
	public ResultVO<?> getUserList(Map<String, Object> params, Integer page, Integer num) {
		Integer total = userDao.count(params);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<Object, Object> map = new HashMap<>();

		List<User> list = userDao.list(params, page1.getStartIndex(), num);
		map.put("list", list);
		map.put("total", total);
		return R.success(map);
	}

	/**
	 * 获取最近3条登录用户
	 */
	@Override
	public ResultVO<?> getRecentLogin() {
		List<Map<String, Object>> recentLogin = userDao.getRecentLogin();
		return R.success(recentLogin);
	}

	@Override
	public boolean checkIsTemporary(UserDto user) {
		boolean f = false;
		// 临时号
		if (user != null && "1".equals(user.getIsTemporary())) {
			f = true;
		}
		return f;
	}

	/**
	 * 注册校验
	 *
	 * @param userDto
	 * @return
	 */
	public ResultVO<?> checkUser(UserDto userDto) {
		String phone = userDto.getPhone();
		String email = userDto.getEmail();
		// String username = userDto.getUsername();
		String isTemporary = userDto.getIsTemporary();
		String machineId = userDto.getMachineId();

		User u = null;

		if ("1".equals(isTemporary)) {
			u = userDao.getByTemporary(userDto);
			if (u != null) {
				return R.error(1, "此手机或者邮箱已注册");
			}
		} else {
			/*
			 * if (!StringUtils.isBlank(username)) { u =
			 * userDao.getUserByUsername(username); if (u != null) { return R.error(1, "此 "
			 * + username + " 已注册"); } }
			 */

			if (!StringUtils.isBlank(phone)) {
				u = userDao.getUserByPhone(userDto.getPhone());
				if (u != null) {
					return R.error(1, "此手机已注册");
				}
			}

			if (!StringUtils.isBlank(email)) {
				u = userDao.getUserByEmail(userDto.getEmail());
				if (u != null) {
					return R.error(1, "此邮箱已注册");
				}
			}
		}

		if (StringUtils.isAllBlank(phone, email)) {
			return R.error(2, "参数错误");
		}
		return R.success(u);
	}

	@Override
	public User getUserHaveGame() {
		User user = UserUtil.getCurrentUser();
		String gameIds = "";
		List<UserGame> ugList = gameDao.getByUidIsDefault(user.getId());
		for (int i = 0; i < ugList.size(); i++) {
			if (i == 0) {
				gameIds = ugList.get(i).getGameId() + "";
			} else {
				gameIds += ";" + ugList.get(i).getGameId();
			}
		}
		user.setGameIds(gameIds);
		user.setPassword(null);
		return user;
	}

	@Override
	public ResultVO<?> syncUser(User user) {
		int i = 0;
		if (user.getId() == null) {
			return R.error(1, "id为空");
		}
		User byId = userDao.getById(user.getId());
		if (byId == null) {
			i = userDao.save(user);
			if (user.getIdentity() != null) {
				List<Long> list = new ArrayList<>();
				list.add(user.getIdentity());
				saveUserRoles(user.getId(), list);
			}
		} else {
			user.setPassword(null);
			i = userDao.update(user);
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success();
	}

	/**
	 * 功能描述:重构用户数据同步
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @date 2020/5/15
	 */
	@Override
	public ResultVO<?> syncUserRpc(UserDto user) {
		Boolean b = false;
		if (user.getId() == null) {
			return R.error(1, "id为空");
		}
		//远程调用
		//String result = restTemplate.getForObject(getUserByIdUrl+user.getId(), String.class);
		//User byId = UserConvertUtil.beansConvert(user,result);
		//本系统中查询
		User user1 = userDao.getById(user.getId());
		//判断是否为在线用户
		String isTemporary = user.getIsTemporary();
		//如果用户不存在
		if(user1==null){
			//判断用户是否未离线,如果是离线直接调用添加功能
			return this.saveUserRpc(user);
		}else {
			//比对判断，如果同步的用户数据是离线,数据库查询出来的是在线，密码和是否联网的状态不能修改（改用户可能已经转了在线）
			if (!"1".equals(user1.getIsTemporary())) {
				//设备不能修改密码
				user.setPassword(null);
				//设备不能修改状态
				user.setIsTemporary(null);
			}
			if (user1.getIsChecked() != null && user1.getIsChecked() == 1) {
				//如果用户是在线用户，手机邮箱等重要字段不能同步
				user.setPassword(null);
				//设备不能修改状态
				user.setIsTemporary(null);
				user.setPhone(null);
				user.setEmail(null);
				user.setPassword(null);
			}
			//判断数据库的更新时间大于传入的时间则不予更新
			Date unityUpdateTime = user.getUpdateTime();
			Date cudUpdateTime = user1.getUpdateTime();
			//如果unity的修改时间为空，不做操作
			if (unityUpdateTime == null) {
				log.error("同步用户:" + user.getId() + ",更新时间为空");
				return R.success();
			} else if (cudUpdateTime == null) {
				this.updateUser1(user);
			} else if (unityUpdateTime.getTime() > cudUpdateTime.getTime()) {
				//如果是unity更新时间大于服务器时间则更新
				return this.updateUser1(user);
			} else {
				return R.success();
			}
			return this.updateUser1(user);
		}
	}

	/**
	 * 功能描述:重构用户保存模块，用户信息保存到用户库中，游戏信息保存到业务系统中
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @date 2020/5/12
	 */
	@Override
	@Transactional
	public ResultVO<?> saveUserRpc(UserDto userDto) {
		ResultVO<?> resultVO = checkUserRpc(userDto);
		if (resultVO.getCode() == 1 || resultVO.getCode() == 2) {
			resultVO.setCode(1);
			return resultVO;
		}
		User user = userDto;
		String uuid = "";

		if (StringUtils.isBlank(user.getId())) {
			//判断用户id,如果不存在，直接返回2020-10-22
			return  R.error(1,"用户id为空");
			//uuid = UUID.randomUUID().toString().replaceAll("-", "");
		} else {
			uuid = user.getId();
		}
		user.setId(uuid);
		//获取接口的版本
		String version = userDto.getVersion();
		if(StringUtils.isBlank(version)){
			//版本为空用旧的代码逻辑
			user.setPassword(passwordEncoderNew(user.getPassword()));
		}
		//本地保存客户业务信息
		userDao.save(user);
		//设置系统为v2代号2
		user.setResourceSys(2);
		this.setCheckColumn(userDto);
		//远程保存
		Boolean result = restTemplate.postForObject(saveUserUrl, user, Boolean.class);
		// 添加用户身份
		List<Long> list = new ArrayList<>();
		list.add(user.getIdentity());
		saveUserRoles(uuid, list);
		// 添加默认游戏
		saveUserGame(userDto);
		Map<String, String> map = new HashMap<>();
		map.put("id", uuid);
		return R.success(map);
	}

	/**
	 * 功能描述:重构用户页面注册
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @date 2020/5/15
	 */
	@Transactional
	@Override
	public ResultVO<?> registerUserRpc(UserDto userDto) {
		//页面注册默认为离线
		if (userDto.getIsChecked() == null) {
			userDto.setIsChecked(0);
		}
		ResultVO<?> resultVO = checkUserRpc(userDto);
		// 1重复 2参数错误
		if (resultVO.getCode() == 1 || resultVO.getCode() == 2) {
			resultVO.setCode(1);
			return resultVO;
		}
		String sha1 = DigestUtils.sha1Hex(userDto.getPassword());
		User user = userDto;
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		User activeUser = UserUtil.getCurrentUser();
		Long identity = 0l;
		//判断年龄是否为空
		if (user.getAge() == null) {
			//如果为空设置成默认值为0
			user.setAge(0);
		}
		// 帮别人注册
		if (activeUser != null) {
			String userId = activeUser.getId();
			identity = activeUser.getIdentity();
			if (identity == ICU) {
				identity = PATIENT;
				user.setIdentity(PATIENT);
				user.setParentId(userId);
			} else if (identity == ADMIN) {
				identity = ICU;
				user.setIdentity(ICU);
				user.setParentId(userId);
			}
			//设置identity
			setIdentityByRole(userDto);
		} else {
			// 自己注册
			identity = user.getIdentity();
			//根据identity推算role
			if (identity == null) {
				return R.error(1, "角色信息为空");
			} else {
				this.setRoleByIdentity(userDto);
			}
		}
		user.setId(uuid);
		user.setPassword(sha1);
		//给用户添加风格
		user.setUserLanguage("cn");
		user.setUserStyle("light");
		//如果当前用户为空会报错，注释
		//setIdentityByRole(userDto);
		//如果有配置场所
		//添加业务信息到业务数据
		userDao.save(user);
		//注册用户时初始化来源系统为V4
		user.setResourceSys(2);
		log.info("[方法]:[{}],[URL]:[{}],[user]:[{}]","调用远程添加用户",saveUserUrl,user);
		//改造成远程调用用户系统
		Boolean result = restTemplate.postForObject(saveUserUrl, user, Boolean.class);
		if (!result) {
			//抛异常
			return R.error(1, "注册失败");
		}
		List<Long> list = new ArrayList<>();
		list.add(identity);
		//如果角色id不为空
		if (!CollectionUtils.isEmpty(this.getRoleIds(userDto))) {
			list = this.getRoleIds(userDto);
		}
		//如果场所id集合不为空
		if (!CollectionUtils.isEmpty(userDto.getRoleIds())) {
			this.saveUserPlace(userDto);
		}
		saveUserRoles(uuid, list);
		// 添加re默认游戏
		saveUserGame(userDto);
		return R.success();
	}

	/**
	 * 功能描述:重构用户修改，远程调用用户服务
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @date 2020/5/12
	 */
	@Override
	@Transactional
	public ResultVO<?> updateUserRpc(UserDto userDto) {
		//本地修改
		userDao.update(userDto);
		if (userDto.getIdentity() != null) {
			List<Long> list = new ArrayList<>();
			list.add(userDto.getIdentity());
			saveUserRoles(userDto.getId(), list);
		}
		updateUserSession(userDto.getId());
		//发送同步消息队列
		String userStr = JSON.toJSONStringWithDateFormat(userDto, DateUtil.DATE_TIME_PATTERN);
		provider.sendTopic(userStr,"update");
		return R.success(userDto);
	}

	/**
	 * 功能描述:api接口修改用户信息
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/5/22 14:19
	 */
	@Override
	public ResultVO<?> updateUser1Rpc(UserDto userDto) {
		String userId = userDto.getId();
		if (userId == null) {
			return R.error(1, "参数错误");
		}
		String status = userDto.getStatus();
		userDao.update(userDto);
		//远程调用
		Boolean result = restTemplate.postForObject(updateUserUrl,userDto, Boolean.class);
		if(!result){
			//更新失败返回
			return R.error(1, "参数错误");
		}
		if (status != null) {
			if (Status.DISABLED.equals(status)) {
				userDao.deleteUserDev(userId);
			} else if (Status.LOCKED.equals(status)) {
				userDao.saveUserRole(userId, DEVELOPER);
			}
		}
		return R.success();
	}

	/**
	 * 功能描述:根据远程调用查询用户信息，并修改缓存中数据
	 *
	 * @param id
	 * @return void
	 * @author larry
	 * @date 2020/5/18
	 */
	private void updateUserSessionRpc(String id) {
		User current = UserUtil.getCurrentUser();
		if (current.getId().equals(id)) {
			//User user = userDao.getById(id);
			//远程中获取
			User user = this.getUserByIdRpc(id);
			UserUtil.setUserSession(user);
			UserUtil.setUserSession(user);
		}
	}

	/**
	 * 功能描述:检查用户是否已经注册
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/6/22 9:28
	 */
	public ResultVO<?> checkUserRpc(UserDto userDto) {
		String phone = userDto.getPhone();
		String email = userDto.getEmail();
		//非空校验
		if (StringUtils.isAllBlank(phone, email)) {
			return R.error(2, "参数错误");
		}
		String isTemporary = userDto.getIsTemporary();
		String machineId = userDto.getMachineId();

		User u = null;
		//初始状态为不认证
		userDto.setIsChecked(0);
		log.info("注册校验,phone:"+phone+",email:"+email+",machineId:"+machineId);
		if ("1".equals(isTemporary) && userDto.getIdentity()==0l) {
			log.info("添加患者,phone:"+userDto.getPhone()+",email:"+email);
			//远程调用
			//离线用户不再做重复性校验
			/*u = userDao.getByTemporary(userDto);
			if (u != null) {
				return R.error(1, "此手机已注册");
			}*/
		} else {
			//在线用户
			User user1;
			//根据email或者手机判断本系统是否存在
			if (StringUtils.isNotBlank(userDto.getEmail())) {
				user1 = userDao.getUserByEmail(userDto.getEmail());
				if (user1 != null) {
					log.info("此邮箱已经注册:" + userDto.getEmail());
					return R.error(1, "此邮箱已经注册");
				}
			}
			if (StringUtils.isNotBlank(userDto.getPhone())) {
				user1 = userDao.getUserByPhone(userDto.getPhone());
				if (user1 != null) {
					log.info("此手机已经注册:" + userDto.getPhone());
					return R.error(1, "此手机已经注册");
				}
			}
			//医生角色且在线用户远程调用
			if (ICU == userDto.getIdentity() || ADMIN == userDto.getIdentity() || DEVELOPER == userDto.getIdentity()) {
				//已在线认证
				userDto.setIsChecked(1);
				//初始化验证的字段
				setCheckColumn(userDto);
				String reuslt = restTemplate.postForObject(getUserByPhoneOrEmailUrl, userDto, String.class);
				//增加用户id判断
				//用户已经存在
				if (StringUtils.isNotBlank(reuslt)) {
					log.info("此用户在远程系统中已经存在" + userDto.toString());
					return R.error(2, "此用户在远程系统中已经存在");
				}
			}
		}
		return R.success(u);
	}

	/**
	 * 功能描述:根据id查询用户信息
	 *
	 * @param id
	 * @return com.zw.admin.server.model.User
	 * @author larry
	 * @date 2020/5/18
	 */
	@Override
	public User getUserByIdRpc(String id) {
		String url = getUserByIdUrl + id;
		String result = restTemplate.getForObject(url, String.class);
		User user = new User();
		user = UserConvertUtil.beansConvert(user, result);
		return user;
	}

	/**
	 * 功能描述:远程删除用户
	 *
	 * @param id
	 * @return java.lang.Boolean
	 * @author larry
	 * @date 2020/5/19
	 */
	@Override
	@Transactional
	public Boolean deleteUserRpc(String id) {
		User user = userDao.getById(id);
		if (user != null) {
			userDao.deleteUserRole(user.getId());
		}else {
			return Boolean.FALSE;
		}
		userDao.deleteUser(id);
		// 删除用户关联游戏
		UserGame userGame = new UserGame();
		userGame.setUserId(id);
		gameDao.delUG(userGame);
		//获取用户是否经过在线验证
		int isChecked = user.getIsChecked();
		if (isChecked == unChecked) {
			//如果用户没有经过在线的验证，远程删除
			String[] ids = {id};
			//删除本地业务数据
			int i = userDao.deleteUser(id);
			//远程调用批量删除
			Boolean b = restTemplate.postForObject(deleteUserByIdsUrl, ids, Boolean.class);
			return b;
		}
		return Boolean.TRUE;
	}

	/**
	 * 功能描述:接收消息，用户数据同步
	 *
	 * @param msg
	 * @return void
	 * @author larry
	 * @Date 2020/6/15 17:32
	 */
	@JmsListener(destination = "ActiveMQTopic")
	public void readTopic(String msg) {
		log.info("接收到主题消息:" + msg);
		if(StringUtils.isBlank(msg)){
			//消息为空直接返回
			return;
		}
		Map<String,String> map = JSONObject.parseObject(msg, Map.class);
		String id = map.get("id");
		String operate = map.get("operate");
		if("v4".equals(id)){
			return;
		}
		String userStr = map.get("msg");
		User user = new User();
		User byId = UserConvertUtil.beansConvert(user, userStr);
		if (StringUtils.isNotBlank(userStr) && "update".equals(operate)){
			if (byId != null) {
				//同步数据
				log.info("消息来自于:"+id+",用户:"+user.getId()+"数据修改");
				userDao.update(byId);
			}
		}
		if(StringUtils.isNotBlank(userStr) && "insert".equals(operate)){
			List<Long> roleIds = user.getRoleIds();
			//治疗师
			if(!CollectionUtils.isEmpty(roleIds) && user.getIdentity() == 1l){
				user.setUserLanguage("cn");
				user.setUserStyle("light");
				log.info("消息来自于:"+id+",用户:"+user.getId()+"数据添加");
				this.save( user);
			}
		}
	}

	/**
	 * 功能描述:发送消息
	 *
	 * @param user
	 * @return void
	 * @author larry
	 * @Date 2020/6/23 15:03
	 */
	void sendUserStr(User user) {
		if(StringUtils.isNotBlank(user.getPassword())){
			String sha = DigestUtils.sha1Hex(user.getPassword());
		}
		//发送同步消息队列
		String userStr = JSON.toJSONStringWithDateFormat(user, DateUtil.DATE_TIME_PATTERN);
		provider.sendTopic(userStr,"update");
	}

	/**
	 * 功能描述:同步用户数据到用户服务
	 *
	 * @param
	 * @return int
	 * @author larry
	 * @Date 2020/7/2 17:22
	 */
	public int initUser() {
		int count = 0;
		//查询所有用户信息
		List<User> users = userDao.getAllUser();
		for (User user : users
		) {
			//远程调用查询是否用户服务由该用户
			String result = restTemplate.getForObject(getUserByIdUrl + user.getId(), String.class);
			//如果没有，远程调用添加
			if (StringUtils.isBlank(result)) {
				log.info("同步患者到用户服务:" + user.toString());
				restTemplate.postForObject(saveUserUrl, user, Boolean.class);
				count++;
			}
		}
		log.info("同步用户数量:"+count);
		return count;
	}

	/**
	 * 功能描述:设置确认字段
	 *
	 * @param userDto
	 * @return void
	 * @author larry
	 * @Date 2020/8/11 11:31
	 */
	public void setCheckColumn(UserDto userDto) {
		Integer isChecked = userDto.getIsChecked();
		if(isChecked==null || (isChecked !=null &&isChecked==0)){
			return;
		}
		String email = userDto.getEmail();
		String phone = userDto.getPhone();
		if (StringUtils.isNotBlank(email) && StringUtils.isBlank(phone)) {
			userDto.setCheckColumn(this.email);
		} else if (StringUtils.isNotBlank(phone) && StringUtils.isBlank(email)) {
			userDto.setCheckColumn(this.phone);
		} else {
			userDto.setCheckColumn(3);
		}
	}

	@Transactional
	public Boolean initUserRpc(String phone, String email, String password){
		//本系统中查询，如果有，直接pass
		User user = null;
		if(StringUtils.isNotBlank(phone)){
			user = userDao.getUserByPhoneOnline(phone);
		}else {
			user = userDao.getUserByEmailOnline(email);
		}
		if(user!=null){
			return Boolean.TRUE;
		}
		//远程查询，远程有，拉取过来
		User user1 = new User();
		user1.setEmail(email);
		user1.setPhone(phone);
		user1.setIsChecked(1);
		String reuslt = restTemplate.postForObject(getUserByPhoneOrEmailUrl, user1, String.class);
		if (StringUtils.isBlank(reuslt)) {
			return Boolean.FALSE;
		}else {
			//远程存在则把数据导入本地库
			//反序列化
			UserDto user2 = JSONObject.parseObject(reuslt, UserDto.class);
			//默认治疗师
			Long identity = ICU;
			user2.setIdentity(ICU);
			userDao.save(user2);
			//保存用户的角色
			List<Long> list = new ArrayList<>();
			list.add(user2.getIdentity());
			saveUserRoles(user2.getId(), list);
			// 添加默认游戏
			saveUserGame(user2);
			//保存用户的默认游戏
			return Boolean.TRUE;
		}

	}

	/**
	 * 功能描述:验证码的校验
	 *
	 * @param userDto
	 * @return java.lang.Boolean
	 * @author larry
	 * @Date 2020/8/11 16:38
	 */
	public Boolean checkVerificationCode(UserDto userDto) {
		String verificationCode = userDto.getVerificationCode();
		if (StringUtils.isBlank(verificationCode)) {
			//验证码为空直接返回
			return Boolean.FALSE;
		}
		String email = userDto.getEmail();
		String phone = userDto.getPhone();
		String address = "";
		if (StringUtils.isNotBlank(email)) {
			address = pre + email;
		}
		if (StringUtils.isNotBlank(phone)) {
			address = pre + phone;
		}
		//redis中获取
		String code = redisTemplate.opsForValue().get(address);
		if (verificationCode.equals(code)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}


	/**
	 * 功能描述:获取用户服务中的数据
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/6 15:25
	 */
	public ResultVO<?> getOnlineUser(UserDto user) {
		//验证码校验
		Boolean flag = this.checkVerificationCode(user);
		if(!flag){
			return R.error(1,"验证码错误");
		}
		//检查本系统的在线用户是否存在
		//设置为在线用户
		user.setIsTemporary("0");
		//设置在线
		user.setIsChecked(checked);
		//如果本系统中已经有在线的用户
		if (checkUserExists(user)) {
			return R.error(1, "邮箱或者手机已经存在");
		}
		//远程获取
		User userFromUserService = getUserFromUserService(user);
		return R.success(userFromUserService);
	}

	/**
	 * 功能描述:验证在线用户本系统是否存在,存在放回true,不存在返回fale
	 *
	 * @param userDto
	 * @return java.lang.Boolean
	 * @author larry
	 * @Date 2020/11/6 14:34
	 */
	public Boolean checkUserExists(UserDto userDto) {
		if(StringUtils.isNotBlank(userDto.getEmail())){
			User userByEmail = userDao.getUserByEmail(userDto.getEmail());
			if(userByEmail!=null){
				//邮箱已经存在
				return Boolean.TRUE;
			}
		}
		if(StringUtils.isNotBlank(userDto.getPhone())){
			User userByPhone = userDao.getUserByEmail(userDto.getPhone());
			if(userByPhone!=null){
				//手机号已经存在
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * 功能描述:根据手机或者邮箱远程系统中获取
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.User
	 * @author larry
	 * @Date 2020/11/6 15:03
	 */
	public User getUserFromUserService(UserDto userDto) {
		String result = restTemplate.postForObject(getUserByPhoneOrEmailUrl, userDto, String.class);
		if (StringUtils.isBlank(result)) {
			return null;
		} else {
			User user = new User();
			user = UserConvertUtil.beansConvert(user, result);
			return user;
		}
	}

	/**
	 * 功能描述:本系统不存在，远程系统中存在
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/7 16:40
	 */
	@Transactional
	public ResultVO<?> updateOnlineUser(UserDto user) {
		if(StringUtils.isBlank(user.getId())){
			return R.error(1,"用户id为空");
		}
		User byId = userDao.getById(user.getId());
		if (byId == null) {
			//设置identity
			this.setIdentityByRole(user);
			//本系统没有，则添加用户
			userDao.save(user);
		} else {
			user.setPassword(null);
			user.setEmail(null);
			user.setPhone(null);
			userDao.update(user);
		}
		//更新角色
		saveUserRoles(user.getId(), user.getRoleIds());
		//添加默认游戏
		saveUserGame(user);
		//保存用户与场所信息
		saveUserPlace(user);
		//远程更新
		String userStr = JSON.toJSONStringWithDateFormat(user, DateUtil.DATE_TIME_PATTERN);
		//发送同步消息队列
		provider.sendTopic(userStr,"update");
		return R.success();
	}

	/**
	 * 功能描述:获取角色id集合
	 *
	 * @param userDto
	 * @return java.util.List<java.lang.Long>
	 * @author larry
	 * @Date 2020/11/7 13:56
	 */
	public List<Long> getRoleIds(UserDto userDto) {
		//如果用户角色集合不为空，则角色要以集合中的为准
		if (!CollectionUtils.isEmpty(userDto.getRoleIds())) {
			return userDto.getRoleIds();
		}
		return null;
	}

	/**
	 * 功能描述:保存用户与场所的关系
	 *
	 * @param userDto
	 * @return void
	 * @author larry
	 * @Date 2020/11/7 13:44
	 */
	public void saveUserPlace(UserDto userDto) {
		if (!CollectionUtils.isEmpty(userDto.getPlaces())) {
			UserPlaceDto userPlaceDto = new UserPlaceDto();
			userPlaceDto.setUserId(userDto.getId());
			userPlaceDto.setPlaces(userDto.getPlaces());
			userPlaceRelService.save(userPlaceDto);
		}
	}

	/**
	 * 功能描述:如果用户角色集合不为空，则角色要以集合中的为准
	 *
	 * @param user
	 * @return void
	 * @author larry
	 * @Date 2020/11/7 16:12
	 */
	public void setIdentityByRole(User user) {
		User currentUser = UserUtil.getCurrentUser();
		String id = currentUser.getId();
		List<Long> roleIds = userDao.selectRoleIdByUserId(id);
		if (!CollectionUtils.isEmpty(roleIds)) {
			if (roleIds.contains(ADMIN)) {
				currentUser.setParentId(id);
			}
		}
		//如果用户角色集合不为空，则角色要以集合中的为准
		if (!CollectionUtils.isEmpty(user.getRoleIds())) {
			//如果角色中有机构管理员
			List<Long> list = user.getRoleIds();
			if (list.contains(ICU)) {
				user.setIdentity(ICU);
			}else if(list.contains(ADMIN)){
				user.setIdentity(ADMIN);
			}else if(list.contains(PATIENT)){
				user.setIdentity(PATIENT);
			}
		}
	}

	/**
	 * 功能描述:根据identity角色初始化
	 *
	 * @param user
	 * @return void
	 * @author larry
	 * @Date 2021/3/19 12:06
	 */
	public void setRoleByIdentity(User user) {
		List<Long> roleIds = new ArrayList<Long>();
		roleIds.add(user.getIdentity());
	}

	/**
	 * 功能描述:离线用户转在线
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/12/7 16:38
	 */
	public ResultVO<?> offlineTurnOnline(UserDto userDto){
		//String id = userDto.getId();
		String password = userDto.getPassword();
		String phone = userDto.getPhone();
		String email = userDto.getEmail();
		String activeCode = userDto.getActiveCode();
		//非空校验
		if((StringUtils.isBlank(phone))  || StringUtils.isBlank(password)){
			log.info("phone:"+phone+",password:"+password);
			return R.error(1,"手机号或密码为空!");
		}
		//active和用户Id必须有一个不为空
		if(StringUtils.isBlank(activeCode)&&StringUtils.isBlank(userDto.getId())){
			log.info("activeCode:"+phone+",id:"+userDto.getId());
			return R.error(1,"用户id或者短链接为空!");
		}
		//判断id是否为空，如果为空走短链接
		String userId = userDto.getId();
		if(StringUtils.isBlank(userId)){
			userId = stringRedisTemplate.opsForValue().get(active_code + activeCode);
			if(StringUtils.isBlank(userId)){
				return  R.error(1,"短链接错误!");
			}
		}
		//校验验证码
		Boolean aBoolean = this.checkVerificationCode(userDto);
		if(!aBoolean){
			return R.error(1,"验证码错误");
		}
		//根据id查询在线账号
		//根据手机或者邮箱加id查询用户，如果不存在提示不匹配
		userDto.setId(userId);
		//User user = userDao.selectUserOnline(userDto);
		User user = userDao.getById(userId);
		//非空验证
		if(user==null){
			return R.error(1,"用户数据为空");
		}
		if(StringUtils.isNotBlank(userDto.getOnlineId())){
			//跳转登录
			return R.success();
		}
		//加密
		password = DigestUtils.sha1Hex(userDto.getPassword());
		userDto.setPassword(password);
		//查询是否有已经在线用户
		UserDto onlineUser = new UserDto();
		onlineUser.setPhone(userDto.getPhone());
		onlineUser.setEmail(userDto.getEmail());
		onlineUser.setIsChecked(checked);
		//onlineUser.setIdentity(PATIENT);
		onlineUser.setUpdateTime(new Date());
		onlineUser.setOnlineId("onlineId");
		//查询离线状态不为空的
		User user1 = userDao.selectUserOnline(onlineUser);
		if (user1 !=null && StringUtils.isNotBlank(user1.getOnlineId())) {
			userDto.setOnlineId(user1.getOnlineId());
			//匹配密码与之前的在线是否相等
			if(!userDto.getPassword().equals(user1.getPassword())){
				//不匹配
				return R.error(1,"所输入密码与已有在线密码不相等!");
			}
		} else {
			userDto.setOnlineId(userId);
		}
		//在线
		userDto.setIsChecked(checked);
		//初始化校验字段
		this.setCheckColumn(userDto);
		//修改在线状态为在线
		userDto.setIsTemporary("0");
		int count = userDao.update(userDto);
		//发送消息给其他系统
		String userStr = JSON.toJSONStringWithDateFormat(userDto, DateUtil.DATE_TIME_PATTERN);
		provider.sendTopic(userStr,"update");
		//删除redis中数据
		if(count>0){
			//删除redis中请求Id与真实id数据
			stringRedisTemplate.delete(active_code + activeCode);
			//删除redis中患者id到真实id的映射
			stringRedisTemplate.delete(userIdPre+userId);
		}
		return R.success();
	}

	/**
	 * 功能描述:根据患者id生成一个激活url地址
	 *
	 * @param userId
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/12/9 17:42
	 */
	@Override
	public ResultVO<?> createActiveUrl(String userId) {
		//非空校验
		if(StringUtils.isBlank(userId)){
			return R.error(1,"患者id为空");
		}
		//合理性校验
		User byId = userDao.getById(userId);
		if(byId==null){
			return R.error(1,"查找不到患者");
		}
		//如果患者已经是在线用户，返回成功
		if(byId.getIsChecked()==1){
			return R.success(offlineToOnline.substring(0,offlineToOnline.length()-1));
		}
		//String code = this.createActiveCode(userId);
		String code = stringRedisTemplate.opsForValue().get(userIdPre + userId);
		if(StringUtils.isNotBlank(code)){
			//根据code查询缓存中是否存在，如果存在，直接返回，不存在继续创建
			String redisUserId = stringRedisTemplate.opsForValue().get(active_code + code);
			if(userId.equals(redisUserId)){
				return R.success(offlineToOnline+code);
			}
		}
		List<String> keysByPattern = this.getKeysByPattern(active_code + "*");
		HashMap<Integer, String> keysMap = new HashMap<>();
		int index=active_code.length();
		HashSet<Integer> set = new HashSet<>();
		for (String s:keysByPattern){
			String substring = s.substring(index);
			keysMap.put(Integer.parseInt(substring),null);
		}
		//Integer max = Collections.max(set);
		for(int i=1;i<Integer.MAX_VALUE;i++){
			boolean b = keysMap.containsKey(i);
			if(!b){
				Boolean saveSuccess = stringRedisTemplate.opsForValue().setIfAbsent(active_code+i,userId);
				if(saveSuccess){
					stringRedisTemplate.expire(active_code+i,3l,TimeUnit.DAYS);
					stringRedisTemplate.opsForValue().set(userIdPre+userId,String.valueOf(i),3,TimeUnit.DAYS);
					log.info("患者:"+userId+",请求id:"+i);
					return R.success(offlineToOnline+i);
				}else {
					continue;
				}
			}
		}
		return R.error(1,"生成连接错误");
	}

	/**
	 * 功能描述:请求码生成
	 *
	 * @param userId
	 * @return java.lang.String
	 * @author larry
	 * @Date 2020/12/9 17:55
	 */
	public String createActiveCode(String userId) {
		for(int i=1;i<activeMaxNum;i++){
			Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(active_code+i,userId);
			if(b){
				//设置过期时间
				stringRedisTemplate.expire(active_code+i,3l,TimeUnit.DAYS);

				log.info("患者:"+userId+",请求id:"+i);
				return String.valueOf(i);
			}else {
				continue;
			}
		}
		return "0";
	}

	/**
	 * 功能描述:根据
	 *
	 * @param pattern
	 * @return java.util.List<java.lang.String>
	 * @author larry
	 * @Date 2020/12/10 16:33
	 */
	public List<String> getKeysByPattern(String pattern) {
		List<String> keys = Lists.newArrayList();
		Cursor<String> cursor = scan(stringRedisTemplate, active_code + "*", 10000);
		while (cursor.hasNext()) {
			//找到一次就添加一次
			keys.add(cursor.next());
		}
		try {
			cursor.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("cursor关闭失败");
		}
		return keys;
	}

	/**
	 * 功能描述:创建游标
	 *
	 * @param stringRedisTemplate
	 * @param match
	 * @param count
	 * @return org.springframework.data.redis.core.Cursor<java.lang.String>
	 * @author larry
	 * @Date 2020/12/10 16:31
	 */
	private static Cursor<String> scan(StringRedisTemplate stringRedisTemplate, String match, int count) {
		ScanOptions scanOptions = ScanOptions.scanOptions().match(match).count(count).build();
		RedisSerializer<String> redisSerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
		return (Cursor) stringRedisTemplate.executeWithStickyConnection((RedisCallback) redisConnection ->
				new ConvertingCursor<>(redisConnection.scan(scanOptions), redisSerializer::deserialize));
	}

	/**
	 * 功能描述:康复系统添加
	 *
	 * @param user
	 * @return java.lang.Boolean
	 * @author larry
	 * @Date 2020/12/24 18:05
	 */
	@Transactional
	@Override
	public Boolean save(User user) {
		//1，保存用户
		userDao.save(user);
		//2，保存角色
		saveUserRoles(user.getId(), user.getRoleIds());
		//3，保存默认游戏
		saveUserGame(user);
		return Boolean.TRUE;
	}

	@Override
	public Boolean merge(User user) {
		// 1.合并用户
		// 如果行作为新记录被插入，则受影响行的值为1；
		// 如果原有的记录被更新，则受影响行的值为2，
		// 如果更新的数据和已有的数据一模一样，则受影响的行数是0
		int userRes = userDao.merge(user);
		// 2.保存角色
		saveUserRoles(user.getId(), user.getRoleIds());
		// 3.如果是新增的数据，则保存默认游戏
		if (userRes == 1) {
			saveUserGame(user);
		}
		return Boolean.TRUE;
	}

	/**
	 * 功能描述:康复系统添加
	 *
	 * @param user
	 * @return java.lang.Boolean
	 * @author larry
	 * @Date 2020/12/24 18:05
	 */
	@Transactional
	@Override
	public Boolean update(UserDto user) {
		//1，保存用户
		userDao.save((User)user);
		//2，保存角色
		saveUserRoles(user.getId(), user.getRoleIds());
		//3，保存默认游戏
		saveUserGame(user);
		return Boolean.TRUE;
	}

	/**
	 * 功能描述:手机号或者邮箱修改
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/12 16:05
	 */
	@Transactional
	public ResultVO<?> updateEmailOrPhone(UserDto user) {
		//更新手机或者邮箱
		String id = user.getId();
		String phone = user.getPhone();
		String email = user.getEmail();
		if (StringUtils.isBlank(id)) {
			return R.error(1, "id为空");
		}
		if (StringUtils.isAllBlank(phone, email)) {
			return R.error(1, "手机和邮箱为空");
		}
		//根据id去查询数据库看是否存在
		User byId = userDao.getById(id);
		if (byId == null) {
			return R.error(1, "手机和邮箱为空");
		}
		//为了避免其他字段被修改，把其他字段设置成Null
		UserDto newUser = new UserDto();
		newUser.setId(user.getId());
		newUser.setEmail(user.getEmail());
		newUser.setPhone(user.getPhone());
		newUser.setIsTemporary(user.getIsTemporary());
		newUser.setUpdateTime(new Date());
		//修改
		if (userDao.update(user) > 0) {
			String userStr = JSON.toJSONStringWithDateFormat(user, DateUtil.DATE_TIME_PATTERN);
			log.info("用户修改前:" + JSON.toJSONStringWithDateFormat(byId, DateUtil.DATE_TIME_PATTERN) + ",用户修改后:" + userStr);
			//同步用户中心
			provider.sendTopic(userStr, "update");
			return R.success();
		} else {
			return R.error(1, "修改失败");
		}
	}


	/**
	 * 功能描述:登录转在线
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/23 11:32
	 */
	public ResultVO<?> loginOrTurnByWxApplet(UserDto userDto) {
		//校验数据是否合理
		String id = userDto.getId();
		String password = userDto.getPassword();
		String phone = userDto.getPhone();
		String activeCode = userDto.getActiveCode();
		//
		if(StringUtils.isBlank(activeCode) || StringUtils.isBlank(userDto.getId()) || StringUtils.isBlank(password) || StringUtils.isBlank(phone)){
			log.info("activeCode:"+phone+",id:"+userDto.getId());
			return R.error(1,"用户id或者激活链接为空或者密码手机号为空!");
		}
		//判断id是否为空，如果为空走短链接
		String openid = stringRedisTemplate.opsForValue().get(activeCode);
		if(StringUtils.isBlank(openid)){
			return R.error(1,"openid为空");
		}
		//根据id查询，如果查询不到，直接返回
		User byId = userDao.getById(id);
		if (byId == null) {
			return R.error(1, "查询不到用户");
		}
		//加密密码
		password = DigestUtils.sha1Hex(userDto.getPassword());
		userDto.setPassword(password);
		//绑定小程序Openid
		userDto.setOpenid(openid);
		//更新时间
		userDto.setUpdateTime(new Date());
		userDto.setIsTemporary("0");
		userDto.setIsChecked(1);
		this.setCheckColumn(userDto);
		UserDto onlineUser = new UserDto();
		onlineUser.setPhone(userDto.getPhone());
		onlineUser.setEmail(userDto.getEmail());
		onlineUser.setIsChecked(checked);
		//onlineUser.setIdentity(PATIENT);
		onlineUser.setUpdateTime(new Date());
		onlineUser.setOnlineId("onlineId");
		//查询离线状态不为空的
		User user1 = userDao.selectUserOnline(onlineUser);
		if (user1 !=null && StringUtils.isNotBlank(user1.getOnlineId())) {
			userDto.setOnlineId(user1.getOnlineId());
			//匹配密码与之前的在线是否相等
			if(!userDto.getPassword().equals(user1.getPassword())){
				//不匹配
				return R.error(1,"所输入密码与已有在线密码不相等!");
			}
		} else {
			userDto.setOnlineId(id);
		}
		//更新用户信息
		userDao.update(userDto);
		String userStr = JSON.toJSONStringWithDateFormat(userDto, DateUtil.DATE_TIME_PATTERN);
		provider.sendTopic(userStr,"update");
		//远程调用
		return R.success();
	}

	/**
	 * 功能描述:根据token获取当前登录的用户数据
	 *
	 * @param loginToken
	 * @return com.zw.admin.server.model.User
	 * @author larry
	 * @Date 2021/5/22 17:44
	 */
	public User getUserByToken(String loginToken) {
		//如果loginToken为空
		if (StringUtils.isBlank(loginToken)) {
			return null;
		}
		//redis中获取token对应的用户数据
		CustomToken token = tokenManager.getToken(loginToken);
		if (token == null) {
			return null;
		}
		String userId = token.getUserId();
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		//查询患者
		UserDto byId = (UserDto) userDao.selectUserDetail(userId);
		//赋予管理员权限
		List<Long> roleIds = new ArrayList<Long>(Arrays.asList(4l));
		byId.setRoleIds(roleIds);
		return byId;
	}
}
