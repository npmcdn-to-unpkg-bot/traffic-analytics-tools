/**
 * 
 */
package com.traffic.analytics.commons.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.traffic.analytics.commons.utils.RequestUtils;

/**
 * 对系统的访问者进行日志的记录，包括ip,uri,parameters等信息
 * @author SEAN
 *
 */
public class LogInterceptor implements HandlerInterceptor {

	protected Log log = LogFactory.getLog(getClass());
	
	protected void writeLog(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String params = RequestUtils.getRequestParam(request);
		log.info("RequestURI : " +uri + "\t" + "RequestParameters : " + params);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		this.writeLog(request);
	}

}
