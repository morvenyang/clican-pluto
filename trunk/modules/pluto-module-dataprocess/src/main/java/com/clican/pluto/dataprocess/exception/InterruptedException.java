/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.exception;

/**
 * 当cluster节点停止导致Spring Context停止的时候Processor会抛出该错误强行终止流程
 * 
 * @author wei.zhang
 * 
 */
public class InterruptedException extends DataProcessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7588354337631372414L;

	public InterruptedException() {
		super();
	}

	public InterruptedException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterruptedException(String message) {
		super(message);
	}

	public InterruptedException(Throwable cause) {
		super(cause);
	}

}

// $Id$