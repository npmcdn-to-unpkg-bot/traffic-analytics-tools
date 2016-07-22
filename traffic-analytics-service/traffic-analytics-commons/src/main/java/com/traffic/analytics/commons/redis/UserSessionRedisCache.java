/**
 * 
 */
package com.traffic.analytics.commons.redis;

import java.util.concurrent.TimeUnit;

import com.traffic.analytics.commons.model.User;

/**
 * 登陆用户的HttpSession缓存在Redis中的操作接口
 * 
 * @author SEAN
 *
 */
public interface UserSessionRedisCache {

	/**
	 * 将用户放置在redis中，默认是30分钟过期时间
	 * 
	 * @param token
	 *            令牌
	 * @param user
	 *            用户数据
	 */
	void put(String token, User user);

	/**
	 * 将用户放置在redis中，默认是30分钟过期时间
	 * 
	 * @param token
	 *            令牌
	 * @param user
	 *            用户数据
	 * @param expireTime
	 *            过期时间
	 * @param timeUnit
	 *            过期时间的单位
	 */
	void put(String token, User user, Long expireTime, TimeUnit timeUnit);

	/**
	 * 刷新用户的token有效时间
	 * 
	 * @param token
	 *            令牌
	 * @param user
	 *            用户数据
	 */
	void refreshTokenExpire(String token, User user);

	/**
	 * 删除用户的token
	 * 
	 * @param token
	 *            令牌
	 */
	void remove(String token);

	/**
	 * 根据token获取用户的数据
	 * 
	 * @param token
	 *            令牌
	 * @return 用户数据
	 */
	User get(String token);

}
