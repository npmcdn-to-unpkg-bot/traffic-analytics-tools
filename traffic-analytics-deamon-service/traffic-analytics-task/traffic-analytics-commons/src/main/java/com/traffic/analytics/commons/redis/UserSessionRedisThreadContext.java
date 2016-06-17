/**
 * 
 */
package com.traffic.analytics.commons.redis;

import com.traffic.analytics.commons.model.User;

/**
 * 用户Session信息的线程变量，此处和Redis是保持同步的
 * 
 * @author SEAN
 */
public class UserSessionRedisThreadContext {

	private static final ThreadLocal<User> u = new ThreadLocal<User>();

	public static void setUser(User user) {
		u.set(user);
	}

	public static User getUser() {
		//TODO
		User user = new User();
		user.setId("56e94573d4c66fcd8dc9ce01");
		user.setAccount("semtools");
		user.setPassword("aztechx");
		user.setUsername("琅韵科技");
		return user;
		//return u.get();
	}

	public static void clear() {
		if (u.get() != null) {
			u.remove();
		}
	}

}
