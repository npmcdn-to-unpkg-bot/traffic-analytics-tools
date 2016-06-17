/**
 * 
 */
package com.traffic.analytics.commons.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SEAN
 *
 */
public class RequestUtils {
	public static final String getRequestParam(HttpServletRequest request) {
		Enumeration<String> params = request.getParameterNames();
		StringBuffer param = new StringBuffer();
		while (params.hasMoreElements()) {
			String paramKey = params.nextElement();
			String paramValue = request.getParameter(paramKey);
			param.append(paramKey).append("=").append(paramValue).append(", ");
		}
		return param.length() > 2 ? param.subSequence(0, param.length() - 2).toString() : param.toString();
	}

}