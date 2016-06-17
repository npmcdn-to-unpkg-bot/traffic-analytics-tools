/**
 * 
 */
package com.traffic.analytics.commons.utils;

import org.jasypt.util.digest.Digester;

/**
 * 用户登录密码加密
 * 
 * @author SEAN
 *
 */
public class PasswordBuilder {

	/**
	 * 密码掺杂了系统自定义的字符前缀
	 */
	private static final String PASSWORD_PREFIX = "http://www.aztechx.com";
	
	
	/**
	 * 生成密码
	 * @param password	密码字符串(注册或新增时候的明文)
	 * @return 生成的密码
	 */
	public String build(String password){
		return new String(new Digester("SHA1").digest((PASSWORD_PREFIX + password).getBytes()));
	}
	
	
}
