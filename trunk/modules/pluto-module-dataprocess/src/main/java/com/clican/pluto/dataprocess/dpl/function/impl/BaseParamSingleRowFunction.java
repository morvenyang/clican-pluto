/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;

import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 自动映射参数的单行处理函数的基类,子类必须自己提供一个calculate方法,参数可以自定义.
 * 
 * @author wei.zhang
 * 
 */
public abstract class BaseParamSingleRowFunction extends BaseSingleRowFunction {

	public final static String CALCULATE_METHOD_NAME = "calculate";

	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Object[] arg = new Object[this.pasList.size()];
		Class<?>[] classType = new Class<?>[this.pasList.size()];
		for (int i = 0; i < pasList.size(); i++) {
			arg[i] = pasList.get(i).getValue(row);
			if (arg[i] != null) {
				classType[i] = arg[i].getClass();
			} else {
				classType[i] = Object.class;
			}
		}
		try {
			Object result = MethodUtils.invokeMethod(this, CALCULATE_METHOD_NAME, arg, classType);
			return result;
		} catch (Exception e) {
			throw new CalculationException(e);
		}
	}
}

// $Id$