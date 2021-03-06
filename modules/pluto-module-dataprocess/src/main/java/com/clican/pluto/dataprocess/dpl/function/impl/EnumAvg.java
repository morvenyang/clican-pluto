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
 * 枚举行求平均值
 * <p>
 * enumAvg(double d1,double d2,double d3...)
 *
 * @author clican
 *
 */
public class EnumAvg extends BaseSingleRowFunction {

	
	public Object calculate(Map<String, Object> row) throws CalculationException,PrefixAndSuffixException{
		Double sum = 0d;
		int size = 0;
		for (PrefixAndSuffix pas : this.pasList) {
			Number value = pas.getValue(row);
			sum += value.doubleValue();
			size++;
		}
		return sum / size;
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		if (this.pasList.size() == 0) {
			throw new DplParseException("EnumAvg至少需要一个Double参数");
		}
	}

}

// $Id: EnumAvg.java 13265 2010-05-26 10:09:21Z wei.zhang $