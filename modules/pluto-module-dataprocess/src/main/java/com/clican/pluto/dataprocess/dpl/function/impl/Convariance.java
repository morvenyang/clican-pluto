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

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算Convariance
 * <p>
 * 
 * convariance(List<double> list1,List<double> list2)
 * 
 * @author wei.zhang
 *
 */
public class Convariance extends BaseMultiRowFunction {

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
			throw new CalculationException("计算Convariance的数据不完整");
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
		Covariance cov = new Covariance();
		double covValue = cov.covariance(referValueList, estimateValueList, false);
		return covValue;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.estimateVectorPas = pasList.get(0);
		this.referVectorPas = pasList.get(1);
	}

}

// $Id$