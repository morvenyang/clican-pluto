/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.common.exception;

public class PlutoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2089645845746830806L;

	public PlutoException() {
		super();
	}

	public PlutoException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlutoException(String message) {
		super(message);
	}

	public PlutoException(Throwable cause) {
		super(cause);
	}

}

//$Id: PlutoException.java 108 2009-04-21 05:27:31Z clican $