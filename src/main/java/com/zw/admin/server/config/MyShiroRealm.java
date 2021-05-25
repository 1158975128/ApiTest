package com.zw.admin.server.config;

import java.util.*;
import java.util.stream.Collectors;

import com.zw.admin.server.utils.UserConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.zw.admin.server.dao.PermissionDao;
import com.zw.admin.server.dao.RoleDao;
import com.zw.admin.server.dao.UserDao;
import com.zw.admin.server.model.Permission;
import com.zw.admin.server.model.Role;
import com.zw.admin.server.model.User;
import com.zw.admin.server.service.UserService;
import com.zw.admin.server.utils.SpringUtil;
import com.zw.admin.server.utils.UserUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

	@Resource
	RestTemplate restTemplate;
	@Value("${getUserByPhoneOrEmailUrl}")
	private String getUserByPhoneOrEmailUrl;
	@Value("${updateUserUrl}")
	private String updateUserUrl;

	//@Override
	protected AuthenticationInfo doGetAuthenticationInfo1(AuthenticationToken token) throws AuthenticationException {
		CustomToken customToken = (CustomToken) token;

		UserService userService = SpringUtil.getBean(UserService.class);
		UserDao userDao = SpringUtil.getBean(UserDao.class);

		String phone = customToken.getPhone();
		String email = customToken.getEmail();
		String isRemember = customToken.getIsRemember();

		User user = null;

		if (!StringUtils.isBlank(phone)) {
			user = userDao.getUserByPhoneOnline(phone);
		} else if (!StringUtils.isBlank(email)) {
			user = userDao.getUserByEmailOnline(email);
		}
		if (user == null) {
			throw new UnknownAccountException("用户不存在");
		}
		user.setLastLoginTime(new Date());
		user.setIsRemember(isRemember);
		userDao.update(user);
		String passwordEncoderNew = userService.passwordEncoderNew(new String(customToken.getPassword()));
		if (!user.getPassword().equals(passwordEncoderNew)) {
			// passwordEncoder(new String(usernamePasswordToken.getPassword()),
			// user.getSalt()))
			throw new IncorrectCredentialsException("密码错误");
		}
		//根据onlineId查询用户姓名
		if(StringUtils.isNotBlank(user.getOnlineId())){
			User online = userDao.getById(user.getOnlineId());
			if(online !=null){
				user.setName(online.getName());
				user.setOnlineId(online.getOnlineId());
			}
		}
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), null,
				getName());
		UserUtil.setUserSession(user);

		return authenticationInfo;
	}

	/**
	 * 功能描述:
	 *
	 * @param token
	 * @return org.apache.shiro.authc.AuthenticationInfo
	 * @author larry
	 * @date 2020/5/14
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		CustomToken customToken = (CustomToken) token;

		UserService userService = SpringUtil.getBean(UserService.class);
		UserDao userDao = SpringUtil.getBean(UserDao.class);

		String phone = customToken.getPhone();
		String email = customToken.getEmail();
		String isRemember = customToken.getIsRemember();
		String openid = customToken.getOpenid();
		Boolean needPassword = Boolean.TRUE;

		User user = null;
		Map map = new HashMap<String,String>();
		if (!StringUtils.isBlank(phone)) {
			user = userDao.getUserByPhone(phone);
			map.put("phone",phone);
		} else if (!StringUtils.isBlank(email)) {
			user = userDao.getUserByEmail(email);
			map.put("email",email);
		}
		//根据openid获取用户信息
		if (StringUtils.isNotBlank(openid)) {
			user = userDao.getByOpenid(openid);
			//根据openid插叙不到直接抛找不到用户
			if (user == null) {
				throw new UnknownAccountException("用户不存在");
			}
			needPassword = Boolean.FALSE;
		}
		if(user==null){
			User data = new User();
			String commonUserDto = restTemplate.postForObject(getUserByPhoneOrEmailUrl,map, String.class);
			try {
				if (commonUserDto != null) {
					UserConvertUtil.beansConvert(data, commonUserDto);
					user = data;
				} else {
					user = null;
				}
			} catch (Exception e) {
				throw new UnknownAccountException("用户不存在");
			}
			if (user == null) {
				throw new UnknownAccountException("用户不存在");
			}
		}
		user.setLastLoginTime(new Date());
		user.setIsRemember(isRemember);
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setLastLoginTime(new Date());
		updateUser.setIsRemember(isRemember);
		//存入最新更新时间
		userDao.update(updateUser);
		User byId = userDao.getById(user.getId());
		if (byId != null) {
			user.setIdentity(byId.getIdentity());
		}
		//根据用户ID获取配置角色
		List<Long> roleIds = userDao.selectRoleIdByUserId(user.getId());
		//设置角色
		user.setRoleIds(roleIds);
		String passwordEncoderNew = userService.passwordEncoderNew(new String(customToken.getPassword()));
		if (!user.getPassword().equals(passwordEncoderNew) && needPassword) {
			throw new IncorrectCredentialsException("密码错误");
		}

		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), null,
				getName());

		UserUtil.setUserSession(user);

		return authenticationInfo;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.debug("权限配置");

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = UserUtil.getCurrentUser();
		List<Role> roles = SpringUtil.getBean(RoleDao.class).listByUserId(user.getId());
		Set<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toSet());
		authorizationInfo.setRoles(roleNames);
		List<Permission> permissionList = SpringUtil.getBean(PermissionDao.class).listByUserId(user.getId());
		UserUtil.setPermissionSession(permissionList);
		Set<String> permissions = permissionList.stream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
				.map(Permission::getPermission).collect(Collectors.toSet());
		authorizationInfo.setStringPermissions(permissions);

		return authorizationInfo;
	}

	/**
	 * 重写缓存key，否则集群下session共享时，会重复执行doGetAuthorizationInfo权限配置
	 */
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) principals;
		Object object = principalCollection.getPrimaryPrincipal();

		if (object instanceof User) {
			User user = (User) object;

			return "authorization:cache:key:users:" + user.getId();
		}

		return super.getAuthorizationCacheKey(principals);
	}

}
