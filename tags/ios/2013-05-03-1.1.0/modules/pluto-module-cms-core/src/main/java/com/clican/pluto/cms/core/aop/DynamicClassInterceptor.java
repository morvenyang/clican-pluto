/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.aop;

import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

import com.clican.pluto.cms.dao.CommonDao;
import com.clican.pluto.orm.annotation.DynamicClass;
import com.clican.pluto.orm.dynamic.impl.DynamicClassLoader;
import com.clican.pluto.orm.dynamic.inter.IPojo;

public class DynamicClassInterceptor implements IntroductionInterceptor {

	private CommonDao commonDao;

	private DynamicClassLoader dynamicClassLoader;

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setDynamicClassLoader(DynamicClassLoader dynamicClassLoader) {
		this.dynamicClassLoader = dynamicClassLoader;
	}

	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] arguments = invocation.getArguments();
		for (int i = 0; i < arguments.length; i++) {
			Object obj1 = arguments[i];
			if (obj1 == null) {
				continue;
			}
			if (obj1 instanceof Collection) {
				Collection temp = (Collection) obj1;
				if (temp.size() != 0) {
					Object obj2 = temp.toArray()[0];
					if (obj2.getClass() == dynamicClassLoader.loadClass(obj2
							.getClass().getName())) {
						continue;
					}
					if (obj2 instanceof IPojo
							&& obj2.getClass().isAnnotationPresent(
									DynamicClass.class)) {
						arguments[i] = commonDao.reloadPojos(temp);
					}
				}
			} else if (obj1.getClass().isArray()) {
				Object[] temp = (Object[]) obj1;
				if (temp.length != 0) {
					Object obj2 = temp[0];
					if (obj2.getClass() == dynamicClassLoader.loadClass(obj2
							.getClass().getName())) {
						continue;
					}
					if (obj2 instanceof IPojo
							&& obj2.getClass().isAnnotationPresent(
									DynamicClass.class)) {
						arguments[i] = commonDao.reloadPojos((IPojo[]) temp);
					}
				}
			}
			if (obj1 instanceof IPojo
					&& obj1.getClass().isAnnotationPresent(DynamicClass.class)) {
				if (obj1.getClass() == dynamicClassLoader.loadClass(obj1
						.getClass().getName())) {
					continue;
				}
				arguments[i] = commonDao.reloadPojo((IPojo) obj1);
			}
		}
		return invocation.proceed();
	}

	@SuppressWarnings("unchecked")
	public boolean implementsInterface(Class intf) {
		return true;
	}

}

// $Id$