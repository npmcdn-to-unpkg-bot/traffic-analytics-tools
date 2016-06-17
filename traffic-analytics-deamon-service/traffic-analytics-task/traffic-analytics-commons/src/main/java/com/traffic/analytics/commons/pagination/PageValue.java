package com.traffic.analytics.commons.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * 对Spring分页的扩展，Spring默认的分页{@link PageRequest}是从0页开始算的，此处修正为1
 * 
 * @author SEAN
 *
 */
public class PageValue {

	private int start;
	private int length;
	private Sort sort;

	public PageValue(int start, int length) {
		this(start, length, null, null);
	}

	public PageValue(int start, int length, String sortField, Direction direction) {
		this.start = start;
		this.length = length;
		if(sortField != null && direction!= null){
			this.sort = new Sort(direction, sortField);	
		}
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
	
}
