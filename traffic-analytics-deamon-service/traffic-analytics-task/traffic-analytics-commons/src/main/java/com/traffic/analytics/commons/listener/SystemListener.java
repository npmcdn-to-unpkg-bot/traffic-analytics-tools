/**
 * 
 */
package com.traffic.analytics.commons.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.traffic.analytics.commons.utils.ApplicationContextUtils;

/**
 * WEB服务器启动时的监听器
 * @author SEAN
 */
public class SystemListener implements ServletContextListener {
    
	@Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationContextUtils.destory();
    }
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 加载应用上下文
        ApplicationContextUtils.init(sce.getServletContext());
        
    }
}
