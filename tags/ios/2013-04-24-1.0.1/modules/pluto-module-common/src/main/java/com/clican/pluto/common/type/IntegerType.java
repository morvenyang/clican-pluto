/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.common.type;

public class IntegerType extends NumberType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6517676379409363890L;
	
	@Override
	public Class<?> getClazz() {
		return java.lang.Integer.class;
	}
}


//$Id$