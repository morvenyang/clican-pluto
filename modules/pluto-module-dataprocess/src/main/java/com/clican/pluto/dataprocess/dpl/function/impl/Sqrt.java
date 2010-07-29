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
 * 计算sqrt数值
 * 
 * @author wei.zhang
 * 
 */
public class Sqrt extends BaseSingleRowFunction {

	/**
	 * 用于计算spart的数值
	 */
	private PrefixAndSuffix value;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Double d = ((Number) value.getValue(row)).doubleValue();
		return Math.sqrt(d);
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		value = this.pasList.get(0);
	}

}

// $Id: Sqrt.java 16262 2010-07-16 09:22:35Z wei.zhang $