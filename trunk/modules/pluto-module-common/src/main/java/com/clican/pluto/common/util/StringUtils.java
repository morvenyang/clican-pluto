/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringUtils {

	/**
	 * Return a string split by the symbol.
	 * 
	 * @param collection
	 * @param symbol
	 * @return the result is combined by collection and symbol
	 */
	public static String getSymbolSplitString(Collection<?> collection,
			String symbol) {
		return getSymbolSplitString(collection.toArray(), symbol);
	}

	public static List<String> getListFromSymbolSplitString(String source,
			String symbol) {
		List<String> result = new ArrayList<String>();
		for (String s : source.split(symbol)) {
			result.add(s);
		}
		return result;
	}

	/**
	 * 
	 * Return a string split by the symbol.
	 * 
	 * @param array
	 * @param symbol
	 * @return the result is combined by array and symbol
	 */
	public static String getSymbolSplitString(Object[] array, String symbol) {
		if (array == null || array.length == 0) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buffer.append(array[i]);
			if (i != array.length - 1) {
				buffer.append(symbol);
			}
		}
		return buffer.toString();
	}

	public static String getGetMethodName(String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
	}

	public static String getIsMethodName(String propertyName) {
		return "is" + propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
	}

	public static String getSetMethodName(String propertyName) {
		return "set" + propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
	}

	public static String getPropertyNameByGetMethodName(String getMethodName) {
		if (getMethodName.startsWith("get")) {
			String propertyName = getMethodName.replaceFirst("get", "");
			propertyName = propertyName.substring(0, 1).toLowerCase()
					+ propertyName.substring(1);
			return propertyName;
		} else if (getMethodName.startsWith("is")) {
			String propertyName = getMethodName.replaceFirst("is", "");
			propertyName = propertyName.substring(0, 1).toLowerCase()
					+ propertyName.substring(1);
			return propertyName;
		} else {
			return getMethodName;
		}

	}

}

// $Id: StringUtils.java 538 2009-06-15 09:33:07Z clican $