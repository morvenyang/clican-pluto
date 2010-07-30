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
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 * 回报率计算函数
 * 
 * 
 * @author jing.tian
 * 
 */
public class ReturnRate extends BaseSingleRowFunction {
	/**
	 * 函数参数
	 */
	private PrefixAndSuffix valuePas;

	private Double previousValue;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Double obj = ((Number)valuePas.getValue(row)).doubleValue();
		if (previousValue == null) {
			previousValue = obj;
			return 0d;
		}
		Double result = (obj - previousValue) / previousValue;
		previousValue = obj;
		return result;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		valuePas = this.pasList.get(0);
		if (this.pasList.size() > 1) {
			previousValue = this.pasList.get(1).getConstantsValue(Double.class);
		}
	}

	
	public boolean isSupportWhere() {
		return false;
	}

}

// $Id: ReturnRate.java 14206 2010-06-06 02:31:05Z wei.zhang $