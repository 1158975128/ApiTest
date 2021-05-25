package com.zw.admin.server.advice;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zw.admin.server.config.CustomToken;
import com.zw.admin.server.model.User;
import com.zw.admin.server.service.TokenManager;
import com.zw.admin.server.utils.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.model.SysLogs;
import com.zw.admin.server.service.SysLogService;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 统一日志处理
 * 
 * @author THF
 *
 *         2017年8月19日
 */
@Aspect
@Component
public class LogAdvice {

	@Autowired
	private SysLogService logService;

	@Around(value = "@annotation(com.zw.admin.server.annotation.LogAnnotation)")
	public Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
		SysLogs sysLogs = new SysLogs();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		String module = null;
		LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
		module = logAnnotation.module();
		if (StringUtils.isEmpty(module)) {
			ApiOperation apiOperation = methodSignature.getMethod().getDeclaredAnnotation(ApiOperation.class);
			if (apiOperation != null) {
				module = apiOperation.value();
			}
		}

		if (StringUtils.isEmpty(module)) {
			throw new RuntimeException("没有指定日志module");
		}
		sysLogs.setModule(module);

		try {
			long beginTime = System.currentTimeMillis();
			Object object = joinPoint.proceed();
			long time = System.currentTimeMillis() - beginTime;
			//增加字段start
			// 请求的方法名
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = methodSignature.getName();
			sysLogs.setMethod(className + "." + methodName + "()");
			// 请求的参数
			Object[] args = joinPoint.getArgs();
			List<Object> logArgs = this.streamOf(args)
					.filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
					.collect(Collectors.toList());
			//过滤后序列化无异常
			String params = JSON.toJSONStringWithDateFormat(logArgs, DateUtil.DATE_TIME_PATTERN, SerializerFeature.WriteDateUseDateFormat);
			sysLogs.setParams(params);
			// 获取request
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
			// 设置IP地址
			sysLogs.setIp(IPUtils.getIpAddr(request));
			// 用户名
			String username = "";
			User user =UserUtil.getCurrentUser();
			if(user!=null){
				username = user.getName();
			}
			sysLogs.setUsername(username);
			sysLogs.setTime(time);
			//增加字段end

			sysLogs.setFlag(true);
			logService.save(sysLogs);

			return object;
		} catch (Exception e) {
			sysLogs.setFlag(false);
			sysLogs.setRemark(e.getMessage());
			logService.save(sysLogs);
			throw e;
		}

	}

	/**
	 * 功能描述:前置通知拦截
	 *
	 * @param joinPoint
	 * @return void
	 * @author larry
	 * @Date 2021/3/10 16:48
	 */
	@Before(value = "@annotation(com.zw.admin.server.annotation.PermissionAccess)")
	public void tokenCheck(JoinPoint joinPoint){
		SysLogs sysLogs = new SysLogs();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// 获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//获取token
		String logintoken = request.getHeader("logintoken");
		if(StringUtils.isEmpty(logintoken)){
			//抛出异常
			 throw new UnknownAccountException("token不存在或者过期");
		}
		//如果token无效直接抛出异常
		TokenManager tokenManager = SpringUtil.getBean(TokenManager.class);
		CustomToken token = tokenManager.getToken(logintoken);
		if(token==null){
			throw new UnknownAccountException("token不存在或者过期");
		}
	}

	public static <T> Stream<T> streamOf(T[] array) {
		return ArrayUtils.isEmpty(array) ? Stream.empty() : Arrays.asList(array).stream();
	}
}
