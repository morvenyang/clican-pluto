/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.dpl.function.FunctionCallback;
import com.clican.pluto.dataprocess.dpl.function.MultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 单行处理函数的基类，主要用来保存columnName
 * 
 * @author wei.zhang
 * 
 */
public abstract class BaseSingleRowFunction extends BaseFunction implements SingleRowFunction {

	public final Object recurseCalculate(final List<Map<String, Object>> rowSet, final Map<String, Object> row) throws CalculationException,
			PrefixAndSuffixException {
		try {
			final Map<String, Object> rowCopy = new HashMap<String, Object>(row);
			for (int i = 0; i < pasList.size(); i++) {
				final PrefixAndSuffix pas = this.pasList.get(i);
				if (pas.getFunction() != null) {
					if (pas.getFunction() instanceof SingleRowFunction) {
						if (pas.getFunction().isLazyCalc()) {
							//在PrefixAndSuffix中会判断如果是FunctionCallback就会调用其getValue()方法
							rowCopy.put(pas.getFunction().getId(), new FunctionCallback() {

								private Object value;

								
								public Object getValue() throws CalculationException, PrefixAndSuffixException {
									if (value == null) {
										value = ((SingleRowFunction) pas.getFunction()).recurseCalculate(rowSet, rowCopy);
									}
									return value;
								}

							});
						} else {
							rowCopy.put(pas.getFunction().getId(), ((SingleRowFunction) pas.getFunction()).recurseCalculate(rowSet, rowCopy));
						}
					} else {
						if (pas.getFunction().isLazyCalc()) {
							//在PrefixAndSuffix中会判断如果是FunctionCallback就会调用其getValue()方法
							rowCopy.put(pas.getFunction().getId(), new FunctionCallback() {
								private Object value;

								
								public Object getValue() throws CalculationException, PrefixAndSuffixException {
									if (value == null) {
										value = ((MultiRowFunction) pas.getFunction()).recurseCalculate(rowSet);
									}
									return value;
								}
							});
						} else {
							rowCopy.put(pas.getFunction().getId(), ((MultiRowFunction) pas.getFunction()).recurseCalculate(rowSet));
						}

					}
				}
			}
			Object result = this.calculate(rowSet, rowCopy);
			if (StringUtils.isNotEmpty(trace)) {
				tracesLog.debug(trace + "=" + result);
			}
			return result;
		} catch (Throwable e) {
			throw new CalculationException("function[" + this.getExpr() + "]", e);
		}

	}

	
	public boolean containMultiRowCalculation() {
		for (PrefixAndSuffix pas : this.pasList) {
			if (pas.getFunction() instanceof MultiRowFunction) {
				return true;
			} else if (pas.getFunction() instanceof SingleRowFunction) {
				boolean result = ((SingleRowFunction) pas.getFunction()).containMultiRowCalculation();
				if (result) {
					return result;
				} else {
					continue;
				}
			}
		}
		return false;
	}

	/**
	 * 调用本身函数
	 * 
	 * @param row
	 * @return
	 * @throws CalculationException
	 */
	public Object calculate(List<Map<String, Object>> rowSet, Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		return calculate(row);
	}
}

// $Id: BaseSingleRowFunction.java 16008 2010-07-12 06:57:28Z wei.zhang $