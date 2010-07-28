/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.exception;

/**
 * 执行Dpl的时候发生错误的该错误被抛出
 * 
 * @author wei.zhang
 * 
 */
public class DplException extends DataProcessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8140352209069089048L;

	public DplException() {
		super();
	}

	public DplException(String message, Throwable cause) {
		super(message, cause);
	}

	public DplException(String message) {
		super(message);
	}

	public DplException(Throwable cause) {
		super(cause);
	}

}

// $Id: DplException.java 12412 2010-05-13 07:01:20Z wei.zhang $