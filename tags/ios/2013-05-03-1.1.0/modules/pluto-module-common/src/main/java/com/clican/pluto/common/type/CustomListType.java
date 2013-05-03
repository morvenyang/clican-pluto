/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.type;

public class CustomListType extends Type {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6799533999039418891L;

	private CustomType customType;

	public CustomListType(CustomType customType) {
		this.customType = customType;
	}

	public CustomType getCustomType() {
		return customType;
	}

	public void setCustomType(CustomType customType) {
		this.customType = customType;
	}


	public String getClassName() {
		return customType.getClassName() + "[]";
	}

	public void setClassName(String className) {
		this.customType = new CustomType(className);
	}

	public String getName() {
		return customType.getName() + "[]";
	}

	@Override
	public String codecProperty() {
		return "class=" + this.getClass().getName() + ";className="
				+ customType.getClassName();
	}

	@Override
	public String getDeclareString() {
		return "List<" + customType.getDeclareString() + ">";
	}

}

// $Id$