/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
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
 * 当第一个参数为True的情况下返回第二个参数否则返回第三个参数
 * <p>
 * 
 * decode(boolean condition,Object trueResult,Object falseResult)
 * 
 * @author clican
 * 
 */
public class Decode extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;

	private PrefixAndSuffix pas2;

	private PrefixAndSuffix pas3;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Boolean result = pas1.getValue(row);
		if (result) {
			return pas2.getValue(row);
		} else {
			return pas3.getValue(row);
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		pas1 = this.pasList.get(0);
		pas2 = this.pasList.get(1);
		pas3 = this.pasList.get(2);
	}

}

// $Id: Decode.java 13261 2010-05-26 10:05:43Z wei.zhang $