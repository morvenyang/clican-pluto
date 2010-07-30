/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.moment.Variance;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算标准差
 * 
 * standardDeviation(double[] values)
 * 
 * 年化标准差
 * 
 * standardDeviation * 365^(1/2)
 * 
 * values - 要计算标准差的数据
 * 
 * @author wei.zhang
 * 
 */
public class StandardDeviation extends BaseMultiRowFunction {

	protected PrefixAndSuffix valuePas;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		if (rowSet.size() == 0) {
			throw new CalculationException("计算SharpeRatio的数据不完整");
		}
		double[] values = new double[rowSet.size()];
		double sum = 0;
		for (int i = 0; i < rowSet.size(); i++) {
			Map<String, Object> row = rowSet.get(i);
			Double value = valuePas.getValue(row);
			values[i] = value;
			sum += value;
		}
		double avg = sum / rowSet.size();
		Variance var = new Variance(false);
		return Math.sqrt(var.evaluate(values, avg));
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		valuePas = this.pasList.get(0);
	}

}

// $Id: StandardDeviation.java 12447 2010-05-13 11:55:06Z wei.zhang $