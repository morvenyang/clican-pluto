/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.type;

public class ClobType extends CommonType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3131526168277921162L;

	@Override
	public Class<?> getClazz() {
		return java.sql.Clob.class;
	}

	@Override
	public String getDeclareString() {
		return "java.lang.String";
	}

}

// $Id$