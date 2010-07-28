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

public class MinOne extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;

	@SuppressWarnings("unchecked")
	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException ,PrefixAndSuffixException{
		Comparable value1 = pas1.getValue(row);
		Comparable value2 = pas2.getValue(row);
		if(value1==null){
			return value2;
		}
		if(value2==null){
			return value1;
		}
		if(value1.compareTo(value2)<=0){
			return value1;
		}else{
			return value2;
		}
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


//$Id: MinOne.java 12410 2010-05-13 06:55:57Z wei.zhang $