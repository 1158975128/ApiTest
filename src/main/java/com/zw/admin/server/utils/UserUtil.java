package com.zw.admin.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.zw.admin.server.constants.UserConstants;
import com.zw.admin.server.dao.PermissionDao;
import com.zw.admin.server.model.Permission;
import com.zw.admin.server.model.User;

public class UserUtil {

	/**
	 * 功能描述:当前系统的用户
	 *
	 * @param
	 * @return com.zw.admin.server.model.User
	 * @author larry
	 * @Date 2020/11/14 16:30
	 */
	public static User getCurrentUser() {
		User user = (User) getSession().getAttribute(UserConstants.LOGIN_USER);
		if (user == null) {
			user = (User) SecurityUtils.getSubject().getPrincipal();
		}
		return user;
	}

	/**
	 * 功能描述:查询用户的角色
	 *
	 * @param
	 * @return java.util.List<java.lang.Long>
	 * @author larry
	 * @Date 2020/11/14 16:30
	 */
	public static List<Long> getRoleIds() {
		User user = getCurrentUser();
		if(user!=null){
			List<Long> roleIds = getCurrentUser().getRoleIds();
			return roleIds;
		}else {
			return new ArrayList<>();
		}

	}

	public static void setUserSession(User user) {
		getSession().setAttribute(UserConstants.LOGIN_USER, user);
	}

	@SuppressWarnings("unchecked")
	public static List<Permission> getCurrentPermissions() {
		List<Permission> list = (List<Permission>) getSession().getAttribute(UserConstants.USER_PERMISSIONS);
		if (CollectionUtils.isEmpty(list)) {
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			list = SpringUtil.getBean(PermissionDao.class).listByUserId(user.getId());
		}
		return list;
	}

	public static void setPermissionSession(List<Permission> list) {
		getSession().setAttribute(UserConstants.USER_PERMISSIONS, list);
	}

	public static Session getSession() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();

		return session;
	}
}
