package com.traffic.analytics.commons.exception;

/**
 * 业务逻辑层的异常
 * 
 * @author SEAN
 */
public class ServiceException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
	}

	public ServiceException(String code) {
		super(code);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String code, Throwable cause) {
		super(code, cause);
	}
}