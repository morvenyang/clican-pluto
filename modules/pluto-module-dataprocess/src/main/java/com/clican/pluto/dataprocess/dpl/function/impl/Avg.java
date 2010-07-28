/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 求平均值的函数
 * 
 * avg(List<Double> values,int start,int end)
 *
 * @author wei.zhang
 *
 */
public class Avg extends BaseMultiRowFunction {

	/**
	 * 函数参数
	 */
	private PrefixAndSuffix prefixAndSuffix;

	private int start = 0;

	private int end = 0;

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		prefixAndSuffix = this.pasList.get(0);
		if (params.size() > 1) {
			start = ((Number) this.pasList.get(1).getConstantsValue()).intValue();
		}
		if (params.size() > 2) {
			end = ((Number) this.pasList.get(2).getConstantsValue()).intValue();
		}
	}

	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		Double result = null;
		int tempEnd = rowSet.size();
		if (end != 0) {
			tempEnd = end;
		}
		for (int i = start; i < tempEnd; i++) {
			Map<String, Object> row = rowSet.get(i);
			Object obj = prefixAndSuffix.getValue(row);
			if (obj == null) {
				continue;
			} else {
				Double temp = null;
				if (obj instanceof Number) {
					temp = ((Number) obj).doubleValue();
				} else {
					throw new CalculationException("不支持的数字类型");
				}
				if (result == null) {
					result = temp;
				} else {
					result += temp;
				}
			}
		}
		if (result == null) {
			return 0d;
		} else {
			return result.doubleValue() / (tempEnd - start);
		}
	}

}

// $Id: Avg.java 13258 2010-05-26 10:01:10Z wei.zhang $