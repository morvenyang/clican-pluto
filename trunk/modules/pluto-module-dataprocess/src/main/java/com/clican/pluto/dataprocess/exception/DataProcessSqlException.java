/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.exception;

/**
 * 当数据处理操作SQL的时候如果产生错误，该Exception会被抛出。
 * 
 * @author wei.zhang
 * 
 */
public class DataProcessSqlException extends DataProcessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4215070994626964714L;

	public DataProcessSqlException() {
		super();
	}

	public DataProcessSqlException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataProcessSqlException(String message) {
		super(message);
	}

	public DataProcessSqlException(Throwable cause) {
		super(cause);
	}

}

// $Id: DataProcessSqlException.java 12410 2010-05-13 06:55:57Z wei.zhang $