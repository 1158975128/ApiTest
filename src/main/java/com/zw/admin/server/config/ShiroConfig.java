package com.zw.admin.server.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.zw.admin.server.filter.LogoutFilter;
import com.zw.admin.server.filter.RestfulFilter;

/**
 * shiro配置
 *
 * @author THF
 */
@Configuration
public class ShiroConfig {

	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/layui/**", "anon");
		filterChainDefinitionMap.put("/register.html", "anon");
		filterChainDefinitionMap.put("/forget.html", "anon");
		filterChainDefinitionMap.put("/tests/**", "anon");
		filterChainDefinitionMap.put("/api/**", "anon");
		filterChainDefinitionMap.put("/log/**", "anon");
		filterChainDefinitionMap.put("/sys/login", "anon");
		filterChainDefinitionMap.put("/sys/login/**", "anon");
		filterChainDefinitionMap.put("/file/**", "anon");
		filterChainDefinitionMap.put("/files", "authc");
		filterChainDefinitionMap.put("/files/**", "anon");
		filterChainDefinitionMap.put("/download/**", "anon");
		filterChainDefinitionMap.put("/downloadPic/**", "anon");
		filterChainDefinitionMap.put("/downloadHeadImage/**", "anon");
		filterChainDefinitionMap.put("/equipments/**", "anon");
		filterChainDefinitionMap.put("/languages/**", "anon");
		filterChainDefinitionMap.put("/devices/**", "anon");
		filterChainDefinitionMap.put("/getRecentLogin", "anon");
		filterChainDefinitionMap.put("/sportRecords/**", "anon");
		filterChainDefinitionMap.put("/dataCollections/**", "anon");
		filterChainDefinitionMap.put("/userTrajectorys/**", "anon");
		filterChainDefinitionMap.put("/userReports/**", "anon");
		filterChainDefinitionMap.put("/users/add", "anon");
		filterChainDefinitionMap.put("/users/save", "anon");
		filterChainDefinitionMap.put("/users/sync", "anon");
		filterChainDefinitionMap.put("/users/userList", "anon");
		filterChainDefinitionMap.put("/users/getUser", "anon");
		filterChainDefinitionMap.put("/users/changePassword", "anon");
		//2020-05-25接口调用删除接口去除认证
		filterChainDefinitionMap.put("/users/delete", "anon");
		//2020-05-25接口调用更新接口去除认证
		filterChainDefinitionMap.put("/users/update", "anon");
		//离线转在线接口
		filterChainDefinitionMap.put("/users/offlineTurnOnline", "anon");
		filterChainDefinitionMap.put("/users/checkUserById", "anon");
		//获取转在短链接，二维码
		filterChainDefinitionMap.put("/users/getQrCodeBase64", "anon");
		//绑定微信小程序
		filterChainDefinitionMap.put("/users/loginOrTurnByWxApplet", "anon");
		filterChainDefinitionMap.put("/wx/qrImageStr", "authc");
		filterChainDefinitionMap.put("/wx/**", "anon");
		filterChainDefinitionMap.put("/users/initUser", "anon");
		filterChainDefinitionMap.put("/users/**", "authc");
		filterChainDefinitionMap.put("/register", "anon");
		filterChainDefinitionMap.put("/redirectWeb", "anon");
		filterChainDefinitionMap.put("/forget", "anon");
		filterChainDefinitionMap.put("/sysIps/**", "anon");
		filterChainDefinitionMap.put("/deviceTypes/**", "anon");
		filterChainDefinitionMap.put("/games/webList", "anon");
		filterChainDefinitionMap.put("/games/save", "authc");
		filterChainDefinitionMap.put("/games/deleteUserGame", "authc");
		filterChainDefinitionMap.put("/games/**", "anon");
		filterChainDefinitionMap.put("/dbereports/**", "anon");
		filterChainDefinitionMap.put("/dbereportdatas/**", "anon");
		//用户默认数据接口
		filterChainDefinitionMap.put("/userDefault/**", "anon");
		//机器信息
		filterChainDefinitionMap.put("/userArchives/**", "anon");
		//驾驶舱加权限
		filterChainDefinitionMap.put("/dataControlCabin/**","authc");
		filterChainDefinitionMap.put("/trainStatss/**", "anon");
		filterChainDefinitionMap.put("/userTrainLogs/**", "anon");
		filterChainDefinitionMap.put("/userTrainReports/**", "anon");
		filterChainDefinitionMap.put("/userTrainEvaluations/**", "anon");
		filterChainDefinitionMap.put("/userLogs/**", "anon");
		filterChainDefinitionMap.put("/machineInfo/**", "anon");
		filterChainDefinitionMap.put("/userAchivements/**", "anon");
		filterChainDefinitionMap.put("/userTrainStates/**", "anon");
		filterChainDefinitionMap.put("/sys/logout", "anon");
		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/version/**", "anon");
		//机器日志接口添加匿名访问
		filterChainDefinitionMap.put("/machineLog/**","anon");
		filterChainDefinitionMap.put("/permissions/**","authc");
		filterChainDefinitionMap.put("/**", "user");
		shiroFilterFactoryBean.setLoginUrl("/notAuthenticated");
		shiroFilterFactoryBean.setSuccessUrl("/index.html");

		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl("/notAuthenticated");

		// 设置authc才会走自定义过滤器!
		RestfulFilter restfulFilter = new RestfulFilter();

		shiroFilterFactoryBean.getFilters().put("authc", restfulFilter);
		//注释退出过滤器
		//shiroFilterFactoryBean.getFilters().put("logout", logoutFilter);

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public SecurityManager securityManager(EhCacheManager cacheManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		securityManager.setCacheManager(cacheManager);
		// securityManager.setSessionManager(sessionManager);
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	@Bean
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		CredentialsMatcher credentialsMatcher = new CredentialsMatcher() {

			@Override
			public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
				// 此前已做判断 此次不需要? 此处待研究
				return true;
			}
		};
		myShiroRealm.setCredentialsMatcher(credentialsMatcher);

		return myShiroRealm;
	}

	/**
	 * 凭证匹配器(原代码)
	 *
	 * @return
	 */
//	@Bean
//	public HashedCredentialsMatcher hashedCredentialsMatcher() {
//		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//
//		hashedCredentialsMatcher.setHashAlgorithmName("RSA");// 散列算法:这里使用MD5算法;
//		hashedCredentialsMatcher.setHashIterations(1);
//
//		return hashedCredentialsMatcher;
//	}

	/**
	 * Shiro生命周期处理器
	 *
	 * @return
	 */
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
	 * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 *
	 * @return
	 */
	@Bean
	@DependsOn({ "lifecycleBeanPostProcessor" })
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * cookie对象
	 * 
	 * @return
	 */
	public SimpleCookie rememberMeCookie() {
		// 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		// 设置cookie的过期时间，单位为秒，这里为一天
		cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));// 7天
		return cookie;
	}

	/**
	 * cookie管理对象
	 * 
	 * @return
	 */
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥
		byte[] decode = Base64.decode("4AvVhmFLUs0KTA3Kprsdag==");
		System.err.println(decode.toString());
		cookieRememberMeManager.setCipherKey(decode);
		return cookieRememberMeManager;
	}

}