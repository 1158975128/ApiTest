package com.zw.admin.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置<br>
 * 集群下启动session共享，需打开@EnableRedisHttpSession<br>
 * 单机下不需要
 * 
 * @author THF
 *
 *         2017年8月10日
 */
@EnableRedisHttpSession
@Configuration
public class RedisConfig {

	@Value("${spring.redis.clients.maxIdle}")
	private int maxIdle;

	@Value("${spring.redis.clients.maxTotal}")
	private int maxTotal;

	@Value("${spring.redis.clients.testOnReturn}")
	private boolean testOnReturn;

	@Value("${spring.redis.clients.testOnBorrow}")
	private boolean testOnBorrow;

	/*
	 * @Value("${spring.redis.password}") private String password;
	 * 
	 * @Value("${spring.redis.masterName}") private String masterName;
	 * 
	 * @Value("${spring.redis.sentinels}") private String sentinels;
	 */
	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Bean
	public JedisPoolConfig jedisPoolConfig() throws Exception {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setTestOnReturn(testOnReturn);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		return jedisPoolConfig;
	}

	// @Autowired
	// private JedisPoolConfig jedisPoolConfig;

	// redis哨兵配置
	// @Bean
	/*
	 * public JedisSentinelPool jedisSentinelPool() {
	 *
	 * String[] host = sentinels.split(","); Set<String> set = new
	 * HashSet<String>(); for (String redisHost : host) { set.add(redisHost); }
	 * JedisSentinelPool configuration = new JedisSentinelPool(masterName, set,
	 * jedisPoolConfig, password); return configuration; }
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean("redisTemplate")
	public RedisTemplate redisTemplate(@Lazy RedisConnectionFactory connectionFactory) {
		RedisTemplate redis = new RedisTemplate();
		GenericToStringSerializer<String> keySerializer = new GenericToStringSerializer<String>(String.class);
		redis.setKeySerializer(keySerializer);
		redis.setHashKeySerializer(keySerializer);
		GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
		redis.setValueSerializer(valueSerializer);
		redis.setHashValueSerializer(valueSerializer);
		redis.setConnectionFactory(connectionFactory);

		return redis;
	}

	@Bean("stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(@Lazy RedisConnectionFactory connectionFactory) {
		StringRedisTemplate redis = new StringRedisTemplate();
		GenericToStringSerializer<String> keySerializer = new GenericToStringSerializer<String>(String.class);
		redis.setKeySerializer(keySerializer);
		redis.setHashKeySerializer(keySerializer);
		GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
		redis.setValueSerializer(valueSerializer);
		redis.setHashValueSerializer(valueSerializer);
		redis.setConnectionFactory(connectionFactory);

		return redis;
	}

}
