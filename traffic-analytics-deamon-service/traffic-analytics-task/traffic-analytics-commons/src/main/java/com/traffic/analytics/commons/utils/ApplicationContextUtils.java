package com.traffic.analytics.commons.utils;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring容器的工具类
 * 
 * @author SEAN
 *
 */
public class ApplicationContextUtils {

	private static final Log log = LogFactory.getLog(ApplicationContextUtils.class);
	private static ApplicationContext ac = null;
	private static boolean initFlag = false;

	private ApplicationContextUtils() {
	}

	/**
	 * 方法说明：初始化环境
	 * 
	 * @param servletContext
	 *            {@link ServletContext}
	 */
	public static void init(ServletContext servletContext) {
		// 上下文已经设置，返回
		if (initFlag)
			return;
		// 获取已经生成的spring上下文信息
		WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		ApplicationContextUtils.ac = wc;
		if (null != wc) {
			log.info("Get the ApplicationContext successful.");
		} else {
			log.error("Get the ApplicationContext faild.");
		}
		// 设置初始化标志为已经初始化
		initFlag = true;
	}

	public static void init(String config) {
		ac = new ClassPathXmlApplicationContext(config);
	}

	/**
	 * 方法说明：返回 Srping应用上下文
	 * 
	 * @return Srping应用上下文
	 */
	public ApplicationContext getApplicationContext() {
		return ac;
	}

	/**
	 * 方法说明：根据bean类型从spring上下文中获取唯一的bean对象，对象必须是唯一的。
	 * 
	 * @param requiredType
	 *            bean类型，可以是接口或者类
	 * @return 相应类型的bean对象
	 */
	public static <T> T getBean(Class<T> requiredType) {
		if (ac != null) {
			return ac.getBean(requiredType);
		}
		return null;
	}

	/**
	 * 方法说明：根据bean名称从spring上下文中获取唯一的bean对象
	 * 
	 * @param beanName
	 *            bean名称
	 * @return object
	 */
	public static Object getBean(String beanName) {
		if (null != ac) {
			return ac.getBean(beanName);
		}
		return null;
	}

	/**
	 * 方法说明：销毁方法
	 */
	public static void destory() {
		ac = null;
		initFlag = false;
	}

}
