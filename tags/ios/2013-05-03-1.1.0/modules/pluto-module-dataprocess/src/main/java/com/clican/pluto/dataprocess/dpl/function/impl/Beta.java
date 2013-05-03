/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.descriptive.moment.Variance;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算贝塔系数
 * 
 * 
 * beta(source, refer)=non-biased-covariance(source, refer)/variance(refer)
 * 
 * @author jing.tian
 * 
 */
public class Beta extends BaseMultiRowFunction {

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
			throw new CalculationException("计算Beta的数据不完整");
		}
		double[] estimateValueList = new double[rowSet.size()];
		double[] referValueList = new double[rowSet.size()];
		for (int i = 0; i < rowSet.size(); i++) {
			Map<String, Object> row = rowSet.get(i);
			Double estimateValue = estimateVectorPas.getValue(row);
			Double referValue = referVectorPas.getValue(row);
			estimateValueList[i] = estimateValue;
			referValueList[i] = referValue;
		}
		Double beta = getBeta(referValueList, estimateValueList);
		return beta;
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

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		this.estimateVectorPas = pasList.get(0);
		this.referVectorPas = pasList.get(1);
	}

}

// $Id: Beta.java 13012 2010-05-24 23:13:44Z wei.zhang $