package com.zw.admin.server.service.impl;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.config.CustomToken;
import com.zw.admin.server.dto.Token;
import com.zw.admin.server.service.TokenManager;

/**
 * redis实现的Token
 * 
 * 
 * @author THF
 *
 *         2017年8月13日
 */
@Primary
@Service
public class RedisTokenManager implements TokenManager {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	private static final String TOKEN_PREFIX = "tokens:";
	/**
	 * token过期秒数
	 */
	@Value("${token.expire.seconds}")
	private Integer expireSeconds;

	@Override
	public Token saveToken(CustomToken usernamePasswordToken) {
		String key = UUID.randomUUID().toString();
		redisTemplate.opsForValue().set(TOKEN_PREFIX + key, JSONObject.toJSONString(usernamePasswordToken),
				expireSeconds, TimeUnit.SECONDS);

		return new Token(key, DateUtils.addSeconds(new Date(), expireSeconds));
	}

	@Override
	public CustomToken getToken(String key) {
		String json = redisTemplate.opsForValue().get(TOKEN_PREFIX + key);
		if (!StringUtils.isEmpty(json)) {
			CustomToken usernamePasswordToken = JSONObject.parseObject(json, CustomToken.class);

			return usernamePasswordToken;
		}
		return null;
	}

	@Override
	public boolean deleteToken(String key) {
		key = TOKEN_PREFIX + key;
		if (redisTemplate.hasKey(key)) {
			redisTemplate.delete(key);

			return true;
		}

		return false;
	}
}
