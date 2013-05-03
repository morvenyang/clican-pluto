/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author E40
 *
 */
package com.clican.pluto.common.util;

import org.apache.commons.lang.StringUtils;

public class TimeUtils {

	/**
	 * 根据时间表达式转换为毫秒
	 * 
	 * @param expr
	 * @return
	 */
	public static long getMillisionSecond(String expr) {
		if (StringUtils.isEmpty(expr)) {
			return -1;
		}
		if (expr.endsWith("ms")) {
			return Long.parseLong(expr.replaceAll(expr, "ms"));
		} else if (expr.endsWith("sec")) {
			expr = expr.replaceAll("sec", "");
			return Long.parseLong(expr) * 1000;
		} else if (expr.endsWith("min")) {
			expr = expr.replaceAll("min", "");
			return Long.parseLong(expr) * 1000 * 60;
		} else if (expr.endsWith("hour")) {
			expr = expr.replaceAll("hour", "");
			return Long.parseLong(expr) * 1000 * 60 * 60;
		} else if (expr.endsWith("day")) {
			expr = expr.replaceAll("day", "");
			return Long.parseLong(expr) * 1000 * 60 * 60 * 24;
		} else if (expr.endsWith("week")) {
			expr = expr.replaceAll("week", "");
			return Long.parseLong(expr) * 1000 * 60 * 60 * 24 * 7;
		} else if (expr.endsWith("month")) {
			expr = expr.replaceAll("month", "");
			return Long.parseLong(expr) * 1000 * 60 * 60 * 24 * 30;
		} else {
			throw new IllegalArgumentException("illegal expression arg");
		}
	}
}

// $Id$