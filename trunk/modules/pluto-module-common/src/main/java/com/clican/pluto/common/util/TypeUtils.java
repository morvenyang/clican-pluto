/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.util;

/**
 * 用于类型转换的工具类
 * 
 * @author wei.zhang
 * 
 */
public class TypeUtils {

	/**
	 * 把一个类型的数字转换成另一个类型的数字
	 * 
	 * @param number
	 *            输入的数字
	 * @param type
	 *            输出的数字的类型
	 * @return
	 */
	public static Number numberToNumber(Number number, Class<?> type) {
		if (type.equals(Integer.class) || type.equals(int.class)) {
			return ((Number) number).intValue();
		} else if (type.equals(Long.class) || type.equals(long.class)) {
			return ((Number) number).longValue();
		} else if (type.equals(Double.class) || type.equals(double.class)) {
			return ((Number) number).doubleValue();
		} else if (type.equals(Float.class) || type.equals(float.class)) {
			return ((Number) number).floatValue();
		} else {
			throw new IllegalArgumentException("type不是预期的类型");
		}
	}

	/**
	 * 把字符串转换成一个数字
	 * 
	 * @param value
	 *            输入的字符串
	 * @param type
	 *            输出的数字的类型
	 * @return
	 */
	public static Number stringToNumber(String value, Class<?> type) {
		if (type.equals(Integer.class) || type.equals(int.class)) {
			return Integer.parseInt((String) value);
		} else if (type.equals(Long.class) || type.equals(long.class)) {
			return Long.parseLong((String) value);
		} else if (type.equals(Double.class) || type.equals(double.class)) {
			return Double.parseDouble((String) value);
		} else if (type.equals(Float.class) || type.equals(float.class)) {
			return Float.parseFloat((String) value);
		} else {
			throw new IllegalArgumentException("type不是预期的类型");
		}
	}
}

// $Id$