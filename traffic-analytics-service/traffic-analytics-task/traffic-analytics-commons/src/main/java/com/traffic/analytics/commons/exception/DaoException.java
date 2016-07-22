package com.traffic.analytics.commons.exception;

/**
 * 数据持久层的异常
 * 
 * @author SEAN
 *
 */
public class DaoException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;

	public DaoException() {
	}

	public DaoException(String code) {
		super(code);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String code, Throwable cause) {
		super(code, cause);
	}

}
