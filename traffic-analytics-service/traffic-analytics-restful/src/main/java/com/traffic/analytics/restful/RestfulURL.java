/**
 * 
 */
package com.traffic.analytics.restful;

/**
 * Restful的资源类，存放了所有的Restful接口的URL(方便统一管理，避免不同模块URL资源重复造成冲突)
 * 
 * @author SEAN
 *
 */
public interface RestfulURL {

	public static String GET_GAVIEW_BY_DOMAIN = "/website/gaview/{domain}";
	
}
