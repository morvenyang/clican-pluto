/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.type;

public class StringArrayType extends CommonType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1240052171946107100L;

	@Override
	public Class<?> getClazz() {
		return String[].class;
	}

	@Override
	public String getClassName() {
		return "java.lang.String[]";
	}

}

// $Id$