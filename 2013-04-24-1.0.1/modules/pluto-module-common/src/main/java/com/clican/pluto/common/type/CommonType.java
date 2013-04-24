/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.type;

public abstract class CommonType extends Type {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5520451276633899116L;

	protected abstract Class<?> getClazz();

	public String getClassName() {
		return getClazz().getName();
	}

	public String getName() {
		return getClazz().getSimpleName();
	}

	@Override
	public String codecProperty() {
		return "class="+getClass().getName();
	}

	@Override
	public String getDeclareString() {
		return getClazz().getName();
	}

	
}

// $Id$