package com.traffic.analytics.commons.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.traffic.analytics.commons.base.model.Model;

@SuppressWarnings("serial")
@Document(collection = "Message")
public class Message extends Model {

	private String code;

	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
