/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.dpl.function.FunctionCallback;
import com.clican.pluto.dataprocess.dpl.function.MultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 多行处理函数的基类，主要用来保存columnName
 * 
 * @author wei.zhang
 * 
 */
public abstract class BaseMultiRowFunction extends BaseFunction implements MultiRowFunction {

	
	public final Object recurseCalculate(final List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		try {
			final List<Map<String, Object>> rowSetCopy = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> row : rowSet) {
				rowSetCopy.add(new HashMap<String, Object>(row));
			}
			for (int i = 0; i < pasList.size(); i++) {
				final PrefixAndSuffix pas = this.pasList.get(i);
				if (pas.getFunction() != null) {
					if (pas.getFunction() instanceof SingleRowFunction) {
						for (int j = 0; j < rowSetCopy.size(); j++) {
							final Map<String, Object> rowCopy = rowSetCopy.get(j);
							if (pas.getFunction().isLazyCalc()) {
								//在PrefixAndSuffix中会判断如果是FunctionCallback就会调用其getValue()方法
								rowCopy.put(pas.getFunction().getId(), new FunctionCallback() {
									private Object value;

									
									public Object getValue() throws CalculationException, PrefixAndSuffixException {
										if (value == null) {
											value = ((SingleRowFunction) pas.getFunction()).recurseCalculate(rowSetCopy, rowCopy);
										}
										return value;
									}
								});
							} else {
								rowCopy.put(pas.getFunction().getId(), ((SingleRowFunction) pas.getFunction()).recurseCalculate(rowSetCopy, rowCopy));
							}
						}
					} else {
						throw new CalculationException("一个多行处理函数的返回数值都是单行结果，单行结果再传递给多行函数来处理没有任何的意义。");
					}
				}
			}
			Object result = this.calculate(rowSetCopy);
			if (StringUtils.isNotEmpty(trace)) {
				tracesLog.debug(trace + "=" + result);
			}
			return result;
		} catch (Throwable e) {
			throw new CalculationException("function[" + this.getExpr() + "]", e);
		}

	}

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

}

// $Id: BaseMultiRowFunction.java 16008 2010-07-12 06:57:28Z wei.zhang $