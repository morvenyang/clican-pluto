/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.type;

public class NumberType extends CommonType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7598922541308531281L;

	@Override
	public Class<?> getClazz() {
		return java.lang.Number.class;
	}

}

// $Id$