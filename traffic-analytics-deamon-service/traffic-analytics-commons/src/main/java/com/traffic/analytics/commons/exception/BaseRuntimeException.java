package com.traffic.analytics.commons.exception;

import com.traffic.analytics.commons.model.Message;
import com.traffic.analytics.commons.returntype.JsonReturn;

/**
 * 运行时异常的封装类，作为系统中异常处理的顶级类，所有异常均从BaseRuntimeException继承，
 * 这里结合了JosnReturn和返回信息给前端调用方的提示功能，{@link JsonReturn}、{@link Message}
 * 
 * @author SEAN
 *
 */
public class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String msg;

	public BaseRuntimeException() {
	}

	public BaseRuntimeException(Throwable cause) {
		super(cause);
	}

	public BaseRuntimeException(String msg) {
		this.msg = msg;
	}

	public BaseRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
