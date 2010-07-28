/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;

public class BeanUtils {

	public static Object getProperty(Object obj, String propertyName)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException {
		Method method = null;
		try {
			method = obj.getClass().getMethod(
					StringUtils.getGetMethodName(propertyName), new Class[] {});
		} catch (NoSuchMethodException e) {
			method = obj.getClass().getMethod(
					StringUtils.getIsMethodName(propertyName), new Class[] {});
		}
		return method.invoke(obj, new Object[] {});
	}

	@SuppressWarnings("unchecked")
	public static Collection<Serializable> getCollectionProperty(Object obj,
			String propertyName) throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		return (Collection<Serializable>) getProperty(obj, propertyName);
	}

	public static Calendar getCalendarProperty(Object obj, String propertyName)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException {
		return (Calendar) getProperty(obj, propertyName);
	}

	public static void setProperty(Object obj, String propertyName,
			Object propertyValue) throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		Method method = obj.getClass().getMethod(
				StringUtils.getSetMethodName(propertyName),
				new Class[] { propertyValue.getClass() });
		method.invoke(obj, new Object[] { propertyValue });
	}

	public static void setCollectionProperty(Object obj, String propertyName,
			Collection<?> coll) throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		Method method = obj.getClass().getMethod(
				StringUtils.getSetMethodName(propertyName),
				new Class[] { Collection.class });
		method.invoke(obj, new Object[] { coll });
	}

}

// $Id$