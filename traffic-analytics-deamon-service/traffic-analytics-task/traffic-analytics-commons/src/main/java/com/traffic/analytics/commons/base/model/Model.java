package com.traffic.analytics.commons.base.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * 所有的数据映射实体都从此类继承，提供了Id的统一管理，公用的例如 {@link #toString()}、{@link #hashCode()}等方法都可以在此处进行扩扎
 * @author SEAN
 *
 */
public abstract class Model implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * MongoDB的主键，默认的情况下是用ObjectId来生成，详情可参考{@link ObjectId#get()}
	 */
    @Id
    protected String id;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
