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
 * 半标准差
 * 
 * @author clican
 * 
 */
public class SemiStandardDeviation extends BaseMultiRowFunction {

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
			throw new CalculationException("计算SemiStandardDeviation的数据不完整");
		}

		Double sum = 0d;
		for (int i = 0; i < rowSet.size(); i++) {
			Double estimate = estimateVectorPas.getValue(rowSet.get(i));
			Double refer = referVectorPas.getValue(rowSet.get(i));
			if (estimate < refer) {
				sum += (estimate - refer) * (estimate - refer);
			}
		}
		Double result = Math.sqrt(sum / rowSet.size());
		if (log.isDebugEnabled()) {
			log.debug("SemiStandardDevition=[" + result + "]");
		}
		return result;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		this.estimateVectorPas = pasList.get(0);
		this.referVectorPas = pasList.get(1);
	}
}

// $Id: SemiStandardDeviation.java 12410 2010-05-13 06:55:57Z wei.zhang $