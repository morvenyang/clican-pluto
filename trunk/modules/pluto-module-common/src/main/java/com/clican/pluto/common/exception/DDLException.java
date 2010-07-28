/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.exception;

public class DDLException extends PlutoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3104549398954884723L;

	public DDLException(String sql, Throwable cause) {
		super("Execute sql[" + sql + "] cause some error occured.", cause);
	}

}

// $Id$