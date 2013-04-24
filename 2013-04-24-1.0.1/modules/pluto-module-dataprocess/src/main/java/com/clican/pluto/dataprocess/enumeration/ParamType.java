/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.enumeration;

/**
 * 定义了<dataprocess:param>标签中可以使用的type属性的枚举值
 * 
 * @author clican
 * 
 */
public enum ParamType {

	DATE("date"),

	STRING("string"),

	DOUBLE("double"),

	LONG("long"),

	INTEGER("int"),

	CLAZZ("clazz"),

	LIST("list"),

	BOOLEAN("boolean");

	private String param;

	private ParamType(String param) {
		this.param = param;
	}

	public static ParamType convert(String param) {
		for (ParamType member : values()) {
			if (member.param.equals(param)) {
				return member;
			}
		}
		return null;
	}
}

// $Id: ParamType.java 16020 2010-07-12 09:18:16Z wei.zhang $