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

/**
 * 求余操作
 * 
 * mod(double d1,double d2)
 * 
 * 等价于
 * d1%d2
 *
 * @author clican
 *
 */
public class Mod extends BaseSingleRowFunction {
	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;

	
	public Object calculate(Map<String, Object> row) throws CalculationException,PrefixAndSuffixException {
		Number value1 = pas1.getValue(row);
		Number value2 = pas2.getValue(row);
		if (value2 == null || value2.doubleValue() == 0) {
			return 0d;
		}
		return value1.doubleValue() % value2.doubleValue();
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


//$Id: Mod.java 14673 2010-06-11 12:49:22Z wei.zhang $