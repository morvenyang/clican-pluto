/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author chulin.gui
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.moment.Variance;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 * 计算指数跟踪误差
 * 
 * @author chulin.gui
 * 
 */
public class TrackError extends BaseMultiRowFunction {

	private PrefixAndSuffix fundNavList;

	private PrefixAndSuffix indexList;

	/**
	 * @param rowSet
	 *            基金和指数日回报率之差的List
	 * @see com.clican.pluto.dataprocess.dpl.function.MultiRowFunction#calculate(java.util.List)
	 */
	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		if (rowSet == null || rowSet.size() == 0) {
			throw new CalculationException("计算指数跟踪误差的数据不完整");
		}

		double[] values = new double[rowSet.size()];
		double sum = 0;
		for (int i = 0; i < rowSet.size(); i++) {
			Map<String, Object> row = rowSet.get(i);
			Double fundValue = fundNavList.getValue(row);
			Double indexValue = indexList.getValue(row);
			double value = fundValue.doubleValue() - indexValue.doubleValue();
			values[i] = value;
			sum += value;
		}
		double avg = sum / (rowSet.size());

		Variance var = new Variance(false);
		Double result = Math.sqrt(var.evaluate(values, avg));

		return result;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		fundNavList = this.pasList.get(0);
		indexList = this.pasList.get(1);
	}

}

// $Id: TrackError.java 12410 2010-05-13 06:55:57Z wei.zhang $