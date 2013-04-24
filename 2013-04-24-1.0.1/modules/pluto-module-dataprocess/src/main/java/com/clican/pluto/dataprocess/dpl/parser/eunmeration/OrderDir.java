/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.eunmeration;

/**
 * 排序的方向的枚举
 *
 * @author clican
 *
 */
public enum OrderDir {

	/**
	 * 升序
	 */
	ASC("asc"),

	/**
	 * 降序
	 */
	DESC("desc");

	private String dir;

	private OrderDir(String dir) {
		this.dir = dir;
	}

	public static OrderDir convert(String dir) {
		for (OrderDir member : values()) {
			if (member.dir.equalsIgnoreCase(dir)) {
				return member;
			}
		}
		return null;
	}
}

// $Id: OrderDir.java 12410 2010-05-13 06:55:57Z wei.zhang $