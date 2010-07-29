/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算RSquare指标
 *
 * @author wei.zhang
 *
 */
public class RSquare extends BaseMultiRowFunction {

	/**
	 * 被预测向量
	 */
	private PrefixAndSuffix estimateVectorPas;

	/**
	 * 被引用向量
	 */
	private PrefixAndSuffix referVectorPas;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		List<Double> estimateValueList = new ArrayList<Double>();
		List<Double> referValueList = new ArrayList<Double>();
		Double estimateSum = 0d;
		Double referSum = 0d;

		for (Map<String, Object> row : rowSet) {
			Double estimateValue = estimateVectorPas.getValue(row);
			Double referValue = referVectorPas.getValue(row);
			estimateSum += estimateValue;
			referSum += referValue;
			estimateValueList.add(estimateSum);
			referValueList.add(referValue);
		}
		Double estimateAvg = estimateSum / estimateValueList.size();
		Double referAvg = referSum / referValueList.size();
		Double upSum = 0d;
		Double downSum = 0d;
		for (int i = 0; i < estimateValueList.size(); i++) {
			Double estimateValue = estimateValueList.get(i);
			Double referValue = referValueList.get(i);
			upSum += (estimateValue - estimateAvg) * (referValue - referAvg);
			downSum += (estimateValue - estimateAvg) * (estimateValue - estimateAvg) * (referValue - referAvg) * (referValue - referAvg);
		}
		Double result = upSum * upSum / downSum;
		return result;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		if (params == null || params.size() != 2) {
			throw new DplParseException();
		}
		this.estimateVectorPas = pasList.get(0);
		this.referVectorPas = pasList.get(1);
	}
}

// $Id: RSquare.java 16259 2010-07-16 09:19:19Z wei.zhang $