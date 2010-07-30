/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author chulin.gui
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
 * compute the internal rate from a given return rate.
 * 
 * RFR = (1 + R) ^(1 / 365) - 1 其中R为7d,3M等收益率的年化收益率
 * 
 * @author chulin.gui
 * 
 */
public class RFR extends BaseSingleRowFunction {

	private PrefixAndSuffix valuePas;

	private int powerNumber = 240;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Double value = valuePas.getValue(row);
		if (value == null) {
			throw new CalculationException("不满足计算条件");
		}
		Double result = Math.pow((value.doubleValue() / 4 + 1), 4.0d / powerNumber) - 1.0;
		return result;
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		this.valuePas = this.pasList.get(0);
		if (this.pasList.size() > 1) {
			powerNumber = ((Number)this.pasList.get(1).getConstantsValue()).intValue();
		}
	}

}

// $Id: RFR.java 12410 2010-05-13 06:55:57Z wei.zhang $