package com.traffic.analytics.commons.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.traffic.analytics.commons.model.User;

/**
 * 登陆用户的HttpSession缓存在Redis中的操作实现类
 * 
 * @author SEAN
 */
@Component
public class UserSessionRedisCacheImpl implements UserSessionRedisCache {

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String,Object> redisTemplate;
	
	//存放过期时间
	private final String SUFFIX_EXPIRE_TIME = "userSessionExpireTime";
	//存放过期时间的单位
	private final String SUFFIX_TIME_UNIT = "userSessionExpireTimeUnit";

	
	/**
	 * User对应的Token信息默认缓存时长是30分钟
	 */
	private Long defaultTokenExpire = 30L;
	
	/*(non-Javadoc)
	 * @see com.traffic.analytics.commons.redis.UserSessionRedisCache#put(java.lang.String, com.traffic.analytics.commons.model.User)
	 */
	@Override
	public void put(String token, User user) {
		this.put(token, user, defaultTokenExpire, TimeUnit.MINUTES);
	}

	/*(non-Javadoc)
	 * @see com.traffic.analytics.commons.redis.UserSessionRedisCache#refreshTokenExpire(java.lang.String, com.traffic.analytics.commons.model.User)
	 */
	@Override
	public void refreshTokenExpire(String token, User user) {
		ValueOperations<String, Object> vo = redisTemplate.opsForValue();
		Long expireTime = (Long) vo.get(token + SUFFIX_EXPIRE_TIME);
		TimeUnit timeUnit = (TimeUnit) vo.get(token + SUFFIX_TIME_UNIT);
		vo.set(token, user, expireTime, timeUnit);
	}

	/*(non-Javadoc)
	 * @see com.traffic.analytics.commons.redis.UserSessionRedisCache#remove(java.lang.String)
	 */
	@Override
	public void remove(String token) {
		redisTemplate.delete(token);
		redisTemplate.delete(token + SUFFIX_EXPIRE_TIME);
		redisTemplate.delete(token + SUFFIX_TIME_UNIT);
	}

	/*(non-Javadoc)
	 * @see com.traffic.analytics.commons.redis.UserSessionRedisCache#get(java.lang.String)
	 */
	@Override
	public User get(String token) {
		User user = null;
		if(StringUtils.isBlank(token)){
			return user;
		}
		
		ValueOperations<String, Object> valueOperation = redisTemplate.opsForValue();
		Object obj = valueOperation.get(token);
		if(obj != null){
			user = (User)obj;
		}
		
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.redis.UserSessionRedisCache#put(java.lang.String, com.traffic.analytics.commons.model.User, java.lang.Long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public void put(String token, User user, Long expireTime, TimeUnit timeUnit) {
		ValueOperations<String, Object> valueOperation = redisTemplate.opsForValue();
		valueOperation.set(token, user , expireTime, timeUnit);
		valueOperation.set(token + SUFFIX_EXPIRE_TIME, expireTime, expireTime, timeUnit);
		valueOperation.set(token + SUFFIX_TIME_UNIT, timeUnit, expireTime, timeUnit);
	}

}