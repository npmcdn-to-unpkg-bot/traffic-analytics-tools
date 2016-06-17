package com.traffic.analytics.commons.pagination;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * 分页的Filter,需要在web.xml进行配置，PageableFilter会把分页数据记录在当前线程变量中
 * {@link PageableThreadContext}
 * 
 * @author SEAN
 *
 */
public class PageableFilter implements Filter {
	private Log log = LogFactory.getLog(getClass());

	@Override
	public void destroy() {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 把分页的数据保存到当前的线程中去
		Object start = request.getParameter("start");
		Object length = request.getParameter("length");
		Object column = request.getParameter("column");
		Object order = request.getParameter("order");
		if (start != null && length != null) {
			log.debug("pagination request filter");
			//分页信息
			PageValue pageValue = new PageValue(Integer.parseInt(start.toString()), Integer.parseInt(length.toString()));
			//排序信息
			if(column != null && order != null){
				Direction direction = order.equals("asc") ? Direction.ASC : Direction.DESC;
				Sort sort = new Sort(direction, new String[]{column.toString()});
				pageValue.setSort(sort);	
			}
			PageableThreadContext.setPageValue(pageValue);
		}

		chain.doFilter(request, response);
		// 销毁当前线程的数据
		if (PageableThreadContext.getPageValue() != null) {
			PageableThreadContext.clear();
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// do nothing
	}
}