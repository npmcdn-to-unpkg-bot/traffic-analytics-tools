package com.traffic.analytics.commons.returntype;

import com.alibaba.fastjson.JSON;

/**
 * Restful返回的统一数据格式。
 * 
 * @author SEAN
 *
 */
public class JsonReturn {

	private String msg = "ok";
	private Object data;

	public JsonReturn() {
	}

	public JsonReturn(String msg) {
		setMsg(msg);
	}

	public JsonReturn(Object data) {
		setData(data);
	}

	public JsonReturn(String msg, Object data) {
		setMsg(msg);
		setData(data);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = JSON.toJSON(data);
	}
}
