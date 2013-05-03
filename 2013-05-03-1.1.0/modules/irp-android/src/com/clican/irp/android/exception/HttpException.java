package com.clican.irp.android.exception;

public class HttpException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8775260769980285578L;

	public HttpException() {
		super();
	}

	public HttpException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public HttpException(String detailMessage) {
		super(detailMessage);
	}

	public HttpException(Throwable throwable) {
		super(throwable);
	}

}
