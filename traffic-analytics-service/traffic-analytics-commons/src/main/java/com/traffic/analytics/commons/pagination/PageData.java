/**
 * 
 */
package com.traffic.analytics.commons.pagination;

import java.util.List;

/**
 * 分页信息的包装类(分页返回的值)
 * 
 * @author SEAN
 *
 */
public class PageData<T> {

	private List<T> list;
	
	private long recordsTotal;

	public PageData(List<T> list, long recordsTotal) {
		super();
		this.list = list;
		this.recordsTotal = recordsTotal;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
}
