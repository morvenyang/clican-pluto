package com.clican.irp.android.exception;

public class NotLoginException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8214543231466528502L;

	public NotLoginException() {
		super();
	}

	public NotLoginException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NotLoginException(String detailMessage) {
		super(detailMessage);
	}

	public NotLoginException(Throwable throwable) {
		super(throwable);
	}

}
