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
 * 计算整个分组的累积收益率
 * 
 * @author wei.zhang
 * 
 */
public class TotalReturnRatio extends BaseMultiRowFunction {
	/**
	 * 函数参数
	 */
	private PrefixAndSuffix ratioPas;

	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		if (rowSet == null || rowSet.size() == 0) {
			throw new CalculationException("计算总收益率数据不够");
		}
		Double result = 0d;
		for (Map<String, Object> row : rowSet) {
			Double ratio = ((Number) ratioPas.getValue(row)).doubleValue();
			result = (1 + ratio) * (1 + result) - 1;
		}
		return result;

	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		ratioPas = this.getPrefixeAndSuffix(params).get(0);
	}
}

// $Id: TotalReturnRatio.java 16265 2010-07-16 09:26:12Z wei.zhang $