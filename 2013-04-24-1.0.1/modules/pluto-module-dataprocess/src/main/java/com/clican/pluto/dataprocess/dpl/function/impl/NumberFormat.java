/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class NumberFormat extends BaseSingleRowFunction {

	/**
	 * 函数参数
	 */
	private PrefixAndSuffix valuePas;
	/**
	 * 函数参数
	 */
	private DecimalFormat patten;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Number value = valuePas.getValue(row);
		if (patten != null) {
			return patten.format(value);
		} else {
			return "" + value;
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		if (params == null || !(params.size() == 2 || params.size() == 1)) {
			throw new DplParseException();
		}
		valuePas = this.pasList.get(0);
		if (params.size() == 2) {
			String p = this.pasList.get(1).getConstantsValue();
			patten = new DecimalFormat(p);
		}
	}
}

// $Id: NumberFormat.java 12410 2010-05-13 06:55:57Z wei.zhang $