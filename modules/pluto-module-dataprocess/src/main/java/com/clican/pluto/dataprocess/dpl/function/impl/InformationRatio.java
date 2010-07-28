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

import org.apache.commons.math.stat.descriptive.moment.Variance;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算信息比
 * <p>
 * 第一个参数是被测试的基金数据
 * <p>
 * 第一个参数是被对比的比较指数数据
 * <p>
 * informationRatio(List<Double> estimateList,List<Double> referList)
 * 
 * @author wei.zhang
 * 
 */
public class InformationRatio extends BaseMultiRowFunction {

	/**
	 * 被预测向量
	 */
	private PrefixAndSuffix estimateVectorPas;

	/**
	 * 被引用向量
	 */
	private PrefixAndSuffix referVectorPas;

	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		if (rowSet.size() == 0) {
			throw new CalculationException("计算InformationRatio的数据不完整");
		}
		List<Double> estimateValueList = new ArrayList<Double>();
		List<Double> referValueList = new ArrayList<Double>();
		Double estimateSum = 0d;
		Double referSum = 0d;
		Double differencingSum = 0d;
		double[] differencingList = new double[rowSet.size()];
		for (int i = 0; i < rowSet.size(); i++) {
			Map<String, Object> row = rowSet.get(i);
			Double estimateValue = estimateVectorPas.getValue(row);
			Double referValue = referVectorPas.getValue(row);
			Double differencing = estimateValue - referValue;
			estimateSum += estimateValue;
			referSum += referValue;
			differencingSum += differencing;
			estimateValueList.add(estimateSum);
			referValueList.add(referValue);
			differencingList[i] = differencing;
		}
		Double estimateAvg = estimateSum / estimateValueList.size();
		Double referAvg = referSum / referValueList.size();
		
		Variance var = new Variance(false);
		Double standard = Math.sqrt(var.evaluate(differencingList, differencingSum / differencingList.length));
		Double result = (estimateAvg - referAvg) / standard;
		if (log.isDebugEnabled()) {
			log.debug("FR=[" + estimateAvg + "],BR=[" + referAvg + "],б=[" + standard + "],information=[" + result + "]");
		}
		return result;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.estimateVectorPas = pasList.get(0);
		this.referVectorPas = pasList.get(1);
	}

}

// $Id: InformationRatio.java 13286 2010-05-27 01:04:17Z wei.zhang $