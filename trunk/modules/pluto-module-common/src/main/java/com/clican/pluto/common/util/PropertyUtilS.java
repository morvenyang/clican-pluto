/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 与属性操作相关的辅助类
 * @author wei.zhang
 *
 */
public class PropertyUtilS {

	/**
	 * 取得属性值，支持嵌套如user.name.familyName。
	 * @param bean
	 * @param name
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public static Object getNestedProperty(Object bean, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (bean == null) {
			return null;
		}
		if (bean instanceof Map) {
			if (((Map) bean).containsKey(name)) {
				return ((Map) bean).get(name);
			}
			if (name.contains(".")) {
				return getNestedProperty(((Map) bean).get(name.substring(0, name.indexOf("."))), name.substring(name.indexOf(".") + 1));
			} else {
				return ((Map) bean).get(name);
			}
		} else {
			String methodName;
			if (name.contains(".")) {
				methodName = name.substring(0, name.indexOf("."));
			} else {
				methodName = name;
			}
			methodName = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
			Method method = bean.getClass().getMethod(methodName, new Class[] {});
			Object obj = method.invoke(bean, new Object[] {});
			if (name.contains(".")) {
				return getNestedProperty(obj, name.substring(name.indexOf(".") + 1));
			} else {
				return obj;
			}
		}
	}
}

// $Id$