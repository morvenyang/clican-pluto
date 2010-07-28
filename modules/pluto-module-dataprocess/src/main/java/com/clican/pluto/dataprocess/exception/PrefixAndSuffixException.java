/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.exception;

/**
 * 包装或通过<code>PrefixAndSuffix</code>调用的时候发送错误该错误被抛出
 * 
 * @author wei.zhang
 * 
 */
public class PrefixAndSuffixException extends DplParseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2990912991986507519L;

	public PrefixAndSuffixException() {
		super();
	}

	public PrefixAndSuffixException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrefixAndSuffixException(String message) {
		super(message);
	}

	public PrefixAndSuffixException(Throwable cause) {
		super(cause);
	}

}

// $Id: PrefixAndSuffixException.java 12412 2010-05-13 07:01:20Z wei.zhang $