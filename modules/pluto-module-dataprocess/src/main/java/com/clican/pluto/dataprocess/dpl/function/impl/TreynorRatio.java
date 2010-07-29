/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.descriptive.moment.Variance;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 特雷诺指标
 * 
 * @author wei.zhang
 * 
 */
public class TreynorRatio extends BaseMultiRowFunction {

	/**
	 * 被预测向量
	 */
	private PrefixAndSuffix estimateVectorPas;

	/**
	 * 被引用向量
	 */
	private PrefixAndSuffix referVectorPas;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		if (rowSet.size() == 0) {
			throw new CalculationException("计算TreynorIndex的数据不完整");
		}

		double[] estimateValueList = new double[rowSet.size()];
		double[] referValueList = new double[rowSet.size()];
		Double estimateSum = 0d;
		for (int i = 0; i < rowSet.size(); i++) {
			Map<String, Object> row = rowSet.get(i);
			Double estimateValue = estimateVectorPas.getValue(row);
			Double referValue = referVectorPas.getValue(row);
			estimateSum += estimateValue;
			estimateValueList[i] = estimateValue;
			referValueList[i] = referValue;
		}
		Double beta = getBeta(referValueList, estimateValueList);
		Double frr = (estimateSum / rowSet.size());
		Double result = frr / beta;
		if (log.isDebugEnabled()) {
			log.debug("Beta=[" + beta + "],FRR=[" + frr + "],TreynorRatio=[" + result + "]");
		}
		return result;
	}

	private double getBeta(double[] referValueList, double[] estimateValueList) {
		Variance var = new Variance(false);
		Covariance cov = new Covariance();
		double varValue = var.evaluate(referValueList);
		double covValue = cov.covariance(referValueList, estimateValueList, false);
		if (log.isDebugEnabled()) {
			log.debug("Covariance=[" + covValue + "],Variance=[" + varValue + "]");
		}
		Double beta = covValue / varValue;
		return beta;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.estimateVectorPas = pasList.get(0);
		this.referVectorPas = pasList.get(1);
	}

}

// $Id: TreynorRatio.java 13012 2010-05-24 23:13:44Z wei.zhang $