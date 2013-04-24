/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.exception;

/**
 * 数据处理模块的Exception的基类
 *
 * @author clican
 *
 */
public class DataProcessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 334629316863951768L;

	public DataProcessException() {
		super();
	}

	public DataProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataProcessException(String message) {
		super(message);
	}

	public DataProcessException(Throwable cause) {
		super(cause);
	}

}

// $Id: DataProcessException.java 12410 2010-05-13 06:55:57Z wei.zhang $