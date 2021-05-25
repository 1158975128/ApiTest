package com.zw.admin.server.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

//@Component
public class RedisUtil {
	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	@Autowired
	private JedisSentinelPool jedisSentinelPool;

	/**
	 * 清空库
	 *
	 * @param keysStr
	 */
	public void delBathKeys(String keysStr) {
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			logger.debug("清空缓存库");
			Transaction tx = jedis.multi();
			// 列出所有的key，查找特定的key如：redis.keys("foo")
			Set<String> keys = jedis.keys(keysStr);
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String detailKey = it.next();
				jedis.del(detailKey);
			}
			List<Object> results = tx.exec();
			for (Object object : results) {
				logger.debug("REDIS 事务返回：" + object.toString());
			}
		} catch (Exception e) {
			logger.error("清空库异常", e);
		} finally {
			if (null != jedis) {
				// 释放对象池
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 保存缓存
	 *
	 * @param value
	 * @param key
	 * @param second 过期时间，单位秒（值为null时缺省设置 30分钟） void
	 * @throws @since 1.0.0
	 */
	public boolean save(final String key, final String value, final Integer second) {
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存存储：key={},value={}", key, value);
			// 设置过期时间
			if (second == null) {
				jedis.setex(key, 30 * 60, value);
			} else {

				jedis.setex(key, second, value);
			}
			return true;
		} catch (Exception e) {
			logger.error("保存缓存失败", e);
			return false;
		} finally {
			if (null != jedis) {
				// 释放对象池
				jedis.close();
			}
		}
	}

	/**
	 * 向缓存中设置对象
	 *
	 * @param key
	 * @param obj
	 * @param second
	 * @return
	 */
	public void saveObject(String key, Object obj, Integer second) {
		String jsonStr = JSON.toJSONString(obj);
		save(key, jsonStr, second);
	}

	/**
	 * 只有在不存在这个缓存的时候才保存这个对象
	 *
	 * @param key
	 * @param obj
	 * @param second 过期时间，单位秒（值为null时缺省设置 30分钟)
	 * @return
	 */
	public boolean saveObjectIfNotExists(String key, Object obj, Integer second) {
		String json = JSON.toJSONString(obj);
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存存储：key={},value={}", key, json);
			Long replyCode = jedis.setnx(key, json);
			if (replyCode.equals(0)) {
				// 已存在
				return false;
			}
			// 设置过期时间
			if (second == null) {
				jedis.expire(key, 30 * 60);
			} else {
				jedis.expire(key, second);
			}
			return true;
		} catch (Exception e) {
			logger.error("保存缓存失败", e);
			return false;
		} finally {
			if (null != jedis) {
				// 释放对象池
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 读取缓存
	 *
	 * @param key
	 * @return
	 */
	public String read(final String key) {
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			// 读取数据
			String value = jedis.get(key);
			logger.info("缓存读取：key={},value={}", key, value);
			return value;
		} catch (Exception e) {
			logger.error("读取缓存失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 删除缓存
	 *
	 * @param keys
	 */
	public boolean delete(final String... keys) {
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			// 删除缓存
			Long count = jedis.del(keys);
			logger.info("删除键：key={}, 删除数量: {}", keys, count);
			return true;
		} catch (Exception e) {
			logger.error("删除KEY为 " + keys + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 向缓存中设置对象
	 *
	 * @param key
	 * @param obj
	 * @return
	 */
	public boolean setObject(String key, Object obj) {
		Jedis jedis = null;
		try {
			String jsonObj = JSON.toJSONString(obj);
			jedis = jedisSentinelPool.getResource();
			logger.info("setObject 缓存存储：key={}", key);
			jedis.set(key, jsonObj);
			return true;
		} catch (Exception e) {
			logger.error("向缓存中设置对象 " + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 向缓存中设置对象
	 *
	 * @param key
	 * @param obj
	 * @return
	 */
	public boolean setObject(String key, Object obj, int expSecs) {
		Jedis jedis = null;
		try {
			String jsonObj = JSON.toJSONString(obj);
			jedis = jedisSentinelPool.getResource();
			logger.info("setObject 缓存存储：key={}", key);
			jedis.setex(key, expSecs, jsonObj);
			return true;
		} catch (Exception e) {
			logger.error("向缓存中设置对象 " + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 向缓存中设置字符串
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setObject(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			jedis.set(key, value);
			return true;
		} catch (Exception e) {
			logger.error("向缓存中设置字符串" + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 从缓存中获取对象
	 *
	 * @param key
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(String key, Class<T> clazz) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			String value = jedis.get(key);
			if (StringUtils.isBlank(value))
				return null;
			Object obj = JSON.parseObject(value, clazz);
			return (T) obj;
		} catch (Exception e) {
			logger.error("从缓存中获取对象" + key + "失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 向缓存中设置List
	 *
	 * @param key
	 * @param list
	 * @return
	 */
	public boolean setList(String key, List list) {
		Jedis jedis = null;
		try {
			String jsonarray = JSON.toJSONString(list);
			jedis = jedisSentinelPool.getResource();
			logger.info("list 缓存读取：key={}", key);
			jedis.set(key, jsonarray);
			return true;
		} catch (Exception e) {
			logger.error("向缓存中设置对象 " + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 向缓存中设置List
	 *
	 * @param key
	 * @param list
	 * @return
	 */
	public boolean setList(String key, List list, int expSecs) {
		Jedis jedis = null;
		try {
			String jsonarray = JSON.toJSONString(list);
			jedis = jedisSentinelPool.getResource();
			jedis.setex(key, expSecs, jsonarray);
			return true;
		} catch (Exception e) {
			logger.error("向缓存中设置对象 " + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 从缓存中获取List
	 *
	 * @param key
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List getList(String key, Class<T> clazz) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			String value = jedis.get(key);
			if (StringUtils.isBlank(value))
				return null;
			logger.info("缓存读取：key={}, value={}", key, value);
			Object obj = JSON.parseArray(value, clazz);
			return (List<T>) obj;
		} catch (Exception e) {
			logger.error("从缓存中获取List" + key + "失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 添加数据到set中
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addToSet(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存存储：key={}, value={}", key, value);
			jedis.sadd(key, value);
			return true;
		} catch (Exception e) {
			logger.error("添加数据到set中" + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	public boolean addToSet(String key, List<String> values) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			Pipeline pipeline = jedis.pipelined();
			logger.info("缓存存储：key={}, 总数={}", key, values.size());
			for (String value : values) {
				jedis.sadd(key, value);
			}
			List<Object> results = pipeline.syncAndReturnAll();
			if (results.size() != values.size())
				logger.info("addToSet 一组数据失败,总数据:" + values.size() + ", 实际:" + results.size());
			else
				logger.info("addToSet 一组数据成功");
			return true;
		} catch (Exception e) {
			logger.error("添加数据到set 一组中" + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	public boolean addToSet(String key, Set<?> values) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			Pipeline pipeline = jedis.pipelined();
			logger.info("缓存存储：key={}, 总数={}", key, values.size());
			for (Object value : values) {
				if (value != null)
					jedis.sadd(key, value.toString());
			}
			List<Object> results = pipeline.syncAndReturnAll();
			if (results.size() != values.size())
				logger.info("addToSet 一组数据失败,总数据:" + values.size() + ", 实际:" + results.size());
			else
				logger.info("addToSet 一组数据成功");
			return true;
		} catch (Exception e) {
			logger.error("添加数据到set 一组中" + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 从set中删除数据
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean removeFromSet(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			jedis.srem(key, value);
			return true;
		} catch (Exception e) {
			logger.error("从set中删除数据" + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 从set中删除数据
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean removeFromSet(String key, Object value) {
		String json = JSON.toJSONString(value);
		return removeFromSet(key, json);
	}

	/**
	 * 判断value是否在集合中
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean isInSet(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			return jedis.sismember(key, value);
		} catch (Exception e) {
			logger.error("判断value是否在列表中" + key + "失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取集合的元素个数
	 *
	 * @param key
	 * @return
	 */
	public Long getSetSize(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			return jedis.scard(key);
		} catch (Exception e) {
			logger.error("获取集合的元素个数 " + key + "失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取整个集合
	 *
	 * @param key
	 * @return
	 */
	public Set<String> getSet(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取set：所以 values key={}", key);
			return jedis.smembers(key);
		} catch (Exception e) {
			logger.error("获取集合的元素个数 " + key + "失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 随机返回set中count个元素 获取的是一个List
	 *
	 * @param key
	 * @param count
	 * @return
	 */
	public List<String> getSetRandValues(String key, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取set：所以 values key={}", key);
			return jedis.srandmember(key, count);
		} catch (Exception e) {
			logger.error("随机返回set:" + key + "中" + count + "个元素失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取 两个SET的并集
	 *
	 * @param key1
	 * @param key2
	 * @return
	 */
	public Set<String> getUnionSetValues(String key1, String key2) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存set中取 key:" + key1 + "与key:" + key2 + "的并集");
			return jedis.sunion(key1, key2);
		} catch (Exception e) {
			logger.error("缓存set中取 key:" + key1 + "与key:" + key2 + "的并集失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取 两个SET的差集
	 *
	 * @param key1
	 * @param key2
	 * @return
	 */
	public Set<String> getDiffSetValues(String key1, String key2) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存set中取 key:" + key1 + "与key:" + key2 + "的差集");
			return jedis.sdiff(key1, key2);
		} catch (Exception e) {
			logger.error("缓存set中取 key:" + key1 + "与key:" + key2 + "的差集失败", e);
			e.printStackTrace();
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取 两个SET的交集
	 *
	 * @param key1
	 * @param key2
	 * @return
	 */
	public Set<String> getSinterSetValues(String key1, String key2) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存set中取 key:" + key1 + "与key:" + key2 + "的交集");
			return jedis.sinter(key1, key2);
		} catch (Exception e) {
			logger.error("缓存set中取 key:" + key1 + "与key:" + key2 + "的交集失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 返回指定map中键值对的数量.
	 *
	 * @param key
	 * @return
	 */
	public Long getMapSize(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			return jedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
			return (long) 0;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 把hashMap中的数据添加到MAP中
	 *
	 * @param mapKey
	 * @param hashMap
	 * @return
	 */
	public boolean addToMap(String mapKey, Map<String, String> hashMap) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			jedis.hmset(mapKey, hashMap);
			return true;
		} catch (Exception e) {
			logger.error("把hashMap中的数据添加到MAP中失败 KEY:" + mapKey, e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 新增key和value的整型键值对 添加数据到MAP中
	 *
	 * @param mapKey
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean addToMap(String mapKey, String field, long value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			jedis.hincrBy(mapKey, field, value);
			return true;
		} catch (Exception e) {
			logger.error("新增key和value的整型键值对 添加数据到MAP中 key=" + mapKey, e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 添加数据到MAP中
	 *
	 * @param mapKey
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean addToMap(String mapKey, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			jedis.hset(mapKey, field, value);
			return true;
		} catch (Exception e) {
			logger.error("新增key和value的整型键值对 添加数据到MAP中 key=" + mapKey, e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取名称为key的hash中所有的键（field）及其对应的value
	 *
	 * @param key
	 * @return
	 */
	public Map<String, String> getMap(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			return jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error("获取名称为" + key + "的hash中所有的键（field）及其对应的value失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 从map中删除key键值对
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean removeFromMap(String key, final String field) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={} field={}", key, field);
			jedis.hdel(key, field);
			logger.info("缓存读取：hash={}", key + "," + field);
			jedis.hdel(key, field);
			return true;
		} catch (Exception e) {
			logger.error("hash中删除key:" + key + "键值对失败", e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * hash中key键的值是否存在
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean isInHash(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={} field={}", key, field);
			return jedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 是否存在key为hash的记录
	 *
	 * @param key
	 * @return
	 */
	public boolean isExistsHash(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			return jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * hash中key键对应的值
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public String getMapValue(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={} field={}", key, field);
			return jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取map中field对应的值
	 *
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<String> getMapValues(String key, final String... fields) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={} fields={}", key, fields);
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取map中的所有值
	 *
	 * @param key
	 * @return
	 */
	public List<String> getMapValues(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			return jedis.hvals(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取hashs中所有的key
	 *
	 * @param key
	 * @return
	 */
	public Set<String> getMapKeys(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("缓存读取：key={}", key);
			return jedis.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 添加数据到LIST中
	 *
	 * @param key
	 * @param index 0 添加到LIST HEAD ,1 添加到LIST TAIL
	 * @param value
	 */
	public boolean addToList(String key, int index, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.debug("增加LIST缓存：key={}" + key + "value:" + value);
			if (index == 0) {
				jedis.lpush(key, value);// 添加到LIST HEAD后进先出
			} else {
				jedis.rpush(key, value);// 添加到LIST TAIL //先进先出
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 加到列表头部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addToListHead(String key, String value) {
		return addToList(key, 0, value);
	}

	/**
	 * 加到列表尾部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addToListTail(String key, String value) {
		return addToList(key, 1, value);
	}

	/**
	 * 把对象添加到LIST中去
	 *
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public boolean addToList(String key, int index, Object value) {
		String json = JSON.toJSONString(value);
		return addToList(key, index, json);
	}

	/**
	 * 添加对象到列表头部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addToListHead(String key, Object value) {
		return addToList(key, 0, value);
	}

	/**
	 * 添加对象到列表尾部
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addToListTail(String key, Object value) {
		return addToList(key, 1, value);
	}

	/**
	 * 获取list中指定下标的元素
	 *
	 * @param listKey
	 * @param index
	 * @return
	 */
	public String getValueFromList(String listKey, long index) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			return jedis.lindex(listKey, index);
		} catch (Exception e) {
			logger.error("LIST 列表中  KEY 为 ：" + listKey + "下标为：" + index + "获取元素失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 从list中获取指定类型的对象
	 *
	 * @param key
	 * @param index
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	public <T> T getObjectFromList(String key, long index, Class<T> clazz) {
		String json = getValueFromList(key, index);
		if (StringUtils.isBlank(json))
			return null;
		return JSON.parseObject(json, clazz);
	}

	/**
	 * 获取并移除list中第一个元素
	 *
	 * @param listKey
	 * @return
	 */
	public String popValueFromList(String listKey) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			return jedis.lpop(listKey);
		} catch (Exception e) {
			logger.error("获取list中第一个元素  KEY 为 ：" + listKey + "失败", e);
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取并移除list中第一个元素
	 *
	 * @param key
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	public <T> T popObjectFromList(String key, Class<T> clazz) {
		String json = popValueFromList(key);
		return JSON.parseObject(json, clazz);
	}

	/**
	 * 返回指定KEY的LIST集合，第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有 begin 0, end
	 * -1表示取得所有
	 *
	 * @param key
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<String> getListValues(String key, int begin, int end) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.debug("缓存读取：hash={}", key + jedis.lrange(key, 0, -1));
			return jedis.lrange(key, begin, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 删除列表指定的值
	 *
	 * @param listKey
	 * @param count   为删除的个数（有重复时）
	 * @param value   删除列表指定的值 ，后add进去的值先被删，类似于出栈
	 * @return
	 */
	public boolean removeFromList(String listKey, long count, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.debug("缓存删除：listkey=" + listKey + "count=" + count + "value=" + value);
			jedis.lrem(listKey, count, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 从列表删除begin到end以外的所有值
	 *
	 * @param listKey
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean removeOutOfRangeFromList(String listKey, int begin, int end) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.debug("缓存删除：listkey=" + listKey + "index=" + begin + "end=" + end);
			jedis.ltrim(listKey, begin, end);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 修改列表中指定下标的值
	 *
	 * @param listKey
	 * @param index
	 * @param value
	 * @return
	 */
	public boolean updateList(String listKey, int index, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			jedis.lset(listKey, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 获取list长度
	 *
	 * @param listKey
	 * @return
	 */
	public Long getListSize(String listKey) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			return jedis.llen(listKey);
		} catch (Exception e) {
			logger.error("LIST 列表中  KEY ：" + listKey + "获取长度失败", e);
			return (long) 0;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 添加元素到SortedSet
	 *
	 * @param key
	 * @param score  权重
	 * @param member value
	 * @return
	 */
	public boolean addToSortedSet(String key, double score, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.debug("增加SortedSet缓存：key={}" + key + "value:" + member + "权重为" + score);
			jedis.zadd(key, score, member);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 添加元素到SortedSet
	 * 
	 * @param
	 * @param members 集合
	 * @return
	 */
	public boolean addToSortedSet(Map<String, Map<String, Double>> members) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			for (Map.Entry<String, Map<String, Double>> entry : members.entrySet()) {
				String key = entry.getKey();
				Map<String, Double> value = entry.getValue();
				if (value == null || value.size() == 0)
					continue;
				logger.debug("增加SortedSet缓存：key={}" + key + "value:" + members);
				jedis.zadd(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 添加元素到SortedSet
	 *
	 * @param key
	 * @param score 权重
	 * @param obj   对象
	 * @return
	 */
	public boolean addToSortedSet(String key, double score, Object obj) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.debug("增加SortedSet缓存：key={}" + key + "value:" + obj + "权重为" + score);
			jedis.zadd(key, score, JSON.toJSONString(obj));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 添加元素到SortedSet
	 *
	 * @param key
	 * @param member values
	 * @return
	 */
	public boolean addToSortedSet(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.debug("增加SortedSet缓存：key={}" + key + "value:" + member);
			jedis.zadd(key, 0, member);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
				jedis = null;
			}
		}
	}

	/**
	 * 从SortedSet中删除指定的元素
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean removeFromSortedSet(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.debug("删除SortedSet缓存：key={}" + key + "value:" + member);
			jedis.zrem(key, member);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * SortedSet 返回指定范围的 成员
	 *
	 * @param key
	 * @param start end
	 * @return 所有(0, -1）
	 */
	public Set<String> getMembersFromSortedSet(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("返回指定范围SortedSet缓存：key={}" + key + "begin:" + start + "end:" + end);
			return jedis.zrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * SortedSet 移除指定范围的 成员
	 *
	 * @param key
	 * @param min start
	 * @param max end
	 * @return 所有(0, -1）
	 */
	public void removeMembersFromSortedSet(String key, double min, double max) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("移除指定权重SortedSet缓存：key={}" + key + "min:" + min + "max:" + max);
			jedis.zremrangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * SortedSet 返回指定范围的成员 按权重指定返回，分页显示
	 *
	 * @param key
	 * @return 所有(0, -1）
	 */
	public Set<String> getMembersFromSortedSetByScore(String key, double min, double max, int offset, int count) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("返回指定范围SortedSet缓存：key={}" + key + "begin:" + offset + "end:" + count + "权重min:" + min
					+ "权重max:" + max);
			return jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * SortedSet 统计zset集合中的元素中个数
	 *
	 * @param key
	 */
	public Long getSortedSetLen(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("查看SortedSet缓存：key={}" + key + "的元素数量");
			return jedis.zcard(key);
		} catch (Exception e) {
			e.printStackTrace();
			return (long) 0;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * SortedSet 统计zset集合中权重某个范围内（1.0——5.0），元素的个
	 *
	 * @param key
	 */
	public Long getSortedSetLen(String key, double min, double max) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("查看SortedSortedSet缓存：key={}" + key + "在" + min + "与" + max + "权重范围内元素数量");
			return jedis.zcount(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			return (long) 0;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * SortedSet 查看集合中元素的权重
	 *
	 * @param key
	 * @param member
	 */
	public Double getMemberScoreFromSortedSet(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			logger.info("查看SortedSortedSet缓存：key={}" + key + " 元素：" + member + "的权重值");
			return jedis.zscore(key, member);
		} catch (Exception e) {
			e.printStackTrace();
			return (double) 0;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 设置过期时间 单位秒
	 *
	 * @param key
	 * @param seconds
	 * @return
	 */
	public boolean expire(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			jedis.expire(key, seconds);
			return true;
		} catch (Exception e) {
			logger.error("设置过期时间 key=" + key, e);
			return false;
		} finally {
			if (null != jedis) {
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 设置缓存在多少时间失效
	 *
	 * @param key
	 * @param times 时间戳
	 * @return
	 */
	public void expireAt(final String key, final Long times) {
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			logger.info("设置生存时间：key={}", key);
			// 设置过期时间
			jedis.expireAt(key, times);
		} catch (Exception e) {
			logger.error("", e.getMessage());
		} finally {
			if (null != jedis) {
				// 释放对象池
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 返回到期的剩余时间
	 *
	 * @param key
	 * @return
	 */
	public Long getRemainingExpireTime(String key) {
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			logger.info("设置生存时间：key={}", key);
			// 设置过期时间
			return jedis.ttl(key);
		} catch (Exception e) {
			logger.error("", e.getMessage());
			return 0L;
		} finally {
			if (null != jedis) {
				// 释放对象池
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 判断某个key是否存在
	 *
	 * @param key
	 * @return
	 */
	public boolean existsKey(final String key) {
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			logger.info("判断key是否存在：key={}", key);
			// 判断key是否存在
			return jedis.exists(key);
		} catch (Exception e) {
			logger.error("判断key:" + key + "是否存在", e);
			return false;
		} finally {
			if (null != jedis) {
				// 释放对象池
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

	// private static final Date

	/**
	 * 获取redis服务器时间
	 *
	 * @return
	 */
	public Date getRedisServerTime() {
		Jedis jedis = null;
		try {
			// 从池中获取对象
			jedis = jedisSentinelPool.getResource();
			List<String> times = jedis.time();
			Integer second = Integer.parseInt(times.get(0));
			Calendar calendar = Calendar.getInstance();
			calendar.set(1970, 0, 1, 0, 0, 0);
			calendar.add(Calendar.SECOND, second);
			Date time = calendar.getTime();
			return time;
		} catch (Exception e) {
			logger.error("获取redis服务器时间失败", e);
			return null;
		} finally {
			if (null != jedis) {
				// 释放对象池
				jedisSentinelPool.returnResource(jedis);
			}
		}
	}

}
