/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算詹森阿尔法值
 * 
 * 该函数参数描述
 * 
 * jensenAlpha(List<double> estimateVector,List<double> referVector1...,List<double> referVector2)
 * 
 * nav - 净值
 * div - 分红
 * split - 拆分
 *
 * @author clican
 *
 */
public class JensenAlpha extends BaseMultiRowFunction {

	/**
	 * 被预测向量
	 */
	private PrefixAndSuffix estimateVectorPas;

	/**
	 * 被关联的多个参照向量
	 */
	private PrefixAndSuffix referVectorPas;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		List<Double> estimateValueList = new ArrayList<Double>();
		List<List<Double>> referValueListSet = new ArrayList<List<Double>>();
		Double estimateSum = 0d;
		List<Double> referSumList = new ArrayList<Double>();
		for (Map<String, Object> row : rowSet) {
			estimateSum = estimateSum + (Double) estimateVectorPas.getValue(row);
			Double value2 = referVectorPas.getValue(row);
			referSumList.add(value2);
		}
		List<Double> betas = new ArrayList<Double>();
		for (int i = 0; i < referValueListSet.size(); i++) {
			List<Double> referValueList = referValueListSet.get(i);
			betas.add(getBeta(referValueList, estimateValueList, referSumList.get(i), estimateSum));
		}
		Double alpha = estimateSum / estimateValueList.size();
		for (int i = 0; i < betas.size(); i++) {
			alpha = alpha - betas.get(i) * referSumList.get(i) / estimateValueList.size();
		}
		return alpha;
	}

	private double getBeta(List<Double> referValueList, List<Double> estimateValueList, Double referSum, Double estimateSum) {
		Double avg1 = referSum / referValueList.size();
		Double avg2 = estimateSum / estimateValueList.size();
		Double sumUp = 0d;
		Double sumDown = 0d;
		for (int i = 0; i < referValueList.size(); i++) {
			Double value1 = referValueList.get(i);
			Double value2 = estimateValueList.get(i);
			sumUp += (value1 - avg1) * (value2 - avg2);
			sumDown += (value1 - avg1) * (value1 - avg1);
		}
		Double beta = sumUp / sumDown;
		return beta;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		if (params == null || params.size() < 2) {
			throw new DplParseException();
		}
		this.estimateVectorPas = pasList.get(0);
		this.referVectorPas = pasList.get(1);
	}
}

// $Id: JensenAlpha.java 16334 2010-07-21 02:04:25Z wei.zhang $