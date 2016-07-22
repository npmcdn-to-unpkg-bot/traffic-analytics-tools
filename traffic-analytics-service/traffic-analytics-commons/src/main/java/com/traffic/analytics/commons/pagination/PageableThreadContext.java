package com.traffic.analytics.commons.pagination;

/**
 * 分页数据的线程变量
 * 
 * @author SEAN
 *
 */
public class PageableThreadContext {

	private static final ThreadLocal<PageValue> p = new ThreadLocal<PageValue>();

	public static void setPageValue(PageValue pageRequest) {
		p.set(pageRequest);
	}

	public static PageValue getPageValue() {
		return p.get();
	}

	public static void clear() {
		if (p.get() != null) {
			p.remove();
		}
	}

}