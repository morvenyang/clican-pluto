/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.exception;

/**
 * 调用Dpl Function的时候发生计算错误
 * 
 * @author clican
 * 
 */
public class CalculationException extends DplParseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6422476671552955172L;

	public CalculationException() {
		super();
	}

	public CalculationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CalculationException(String message) {
		super(message);
	}

	public CalculationException(Throwable cause) {
		super(cause);
	}

}

// $Id: CalculationException.java 12412 2010-05-13 07:01:20Z wei.zhang $