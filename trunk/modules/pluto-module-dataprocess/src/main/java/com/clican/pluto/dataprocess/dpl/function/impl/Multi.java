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
 * 乘法操作
 * multi(double d1,double d2)
 * 
 * 等价于
 * d1*d2
 *
 * @author wei.zhang
 *
 */
public class Multi extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;
	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException,PrefixAndSuffixException {
		Number value1 = pas1.getValue(row);
		Number value2 = pas2.getValue(row);
		Double result = value1.doubleValue()*value2.doubleValue();
		return result;
	}

	@Override
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		pas1 = this.pasList.get(0);
		pas2 = this.pasList.get(1);
	}

}


//$Id: Multi.java 14674 2010-06-11 12:49:53Z wei.zhang $