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
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class Pow extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Number value1 = pas1.getValue(row);
		Number value2 = pas2.getValue(row);
		return Math.pow(value1.doubleValue(), value2.doubleValue());
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		pas1 = this.pasList.get(0);
		pas2 = this.pasList.get(1);
	}

}

// $Id: Pow.java 12410 2010-05-13 06:55:57Z wei.zhang $