/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.exception;

/**
 * 在进行Dpl解析的时候发送错误该错误被抛出
 * 
 * @author wei.zhang
 * 
 */
public class DplParseException extends DplException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 296225841514799529L;

	public DplParseException() {
		super();
	}

	public DplParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DplParseException(String message) {
		super(message);
	}

	public DplParseException(Throwable cause) {
		super(cause);
	}

}

// $Id: DplParseException.java 12412 2010-05-13 07:01:20Z wei.zhang $