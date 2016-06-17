package com.traffic.analytics.commons.exception;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.traffic.analytics.commons.returntype.JsonReturn;
import com.traffic.analytics.commons.utils.ResponseUtils;

/**
 * 统一的异常处理，Spring框架在遇到异常的时候会调用resolveException的方法
 * 
 * @author SEAN
 */
public class ExceptionHandler implements HandlerExceptionResolver {

	private Log log = LogFactory.getLog(getClass());

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			if (handler instanceof HandlerMethod) {
				Method method = ((HandlerMethod) handler).getMethod();
				Class<?> returnType = method.getReturnType();
				if (JsonReturn.class.equals(returnType)) {
					JsonReturn b = new JsonReturn();
					if (ex instanceof DaoException) {
						b.setMsg(((DaoException) ex).getMsg());
					} else if (ex instanceof ServiceException) {
						b.setMsg(((DaoException) ex).getMsg());
					} else {
						b.setMsg("系统错误");
					}

					ResponseUtils.write2Client(response, b);
					return null;
				}
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return null;
	}
}