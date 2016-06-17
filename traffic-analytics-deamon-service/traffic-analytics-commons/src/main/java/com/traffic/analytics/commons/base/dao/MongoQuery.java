/**
 * 
 */
package com.traffic.analytics.commons.base.dao;

/**
 * @author SEAN
 *
 */
public class MongoQuery {

	private String fieldName;
	
	private String fieldType;
	
	private String condition;
	
	private String value;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
