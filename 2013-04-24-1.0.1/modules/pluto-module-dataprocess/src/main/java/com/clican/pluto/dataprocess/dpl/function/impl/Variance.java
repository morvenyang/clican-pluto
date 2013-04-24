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
 * 计算方差
 * 
 * variance(double[] values)
 * 
 * values - 要计算方差的数据
 * 
 * @author clican
 *
 */
public class Variance extends BaseMultiRowFunction {

	private PrefixAndSuffix valuePas;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		double[] values = new double[rowSet.size()];
		double sum = 0;
		for (int i=0;i<rowSet.size();i++) {
			Map<String, Object> row = rowSet.get(i);
			Double value = valuePas.getValue(row);
			values[i]=value;
			sum+=value;
		}
		double avg = sum/rowSet.size();
		org.apache.commons.math.stat.descriptive.moment.Variance var = new org.apache.commons.math.stat.descriptive.moment.Variance(false);
		return var.evaluate(values, avg);
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		if (params == null || params.size() != 1) {
			throw new DplParseException();
		}
		valuePas = this.pasList.get(0);
	}
}

// $Id: Variance.java 12410 2010-05-13 06:55:57Z wei.zhang $