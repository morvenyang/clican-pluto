/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 根据各自的权重来计算符合收益率
 * 
 * 有多行数据传入其中
 * <p>
 * price list就包括多个股票的价格
 * <p>
 * weight list包括多个权重，如果没有权重变化则该行的权重为空
 * <p>
 * code list就包括每行记录对应的代码
 * 
 * yieldByWeight(List<Double> price,List<Double> weight,List<String> code)
 * 
 * 
 * @author clican
 * 
 */
public class YieldByWeight extends BaseMultiRowFunction {

	public final static double DEFAULT_MONEY = 1000d;
	/**
	 * 函数参数
	 */
	protected PrefixAndSuffix pricePas;
	/**
	 * 函数参数
	 */
	protected PrefixAndSuffix weightPas;
	/**
	 * 函数参数
	 */
	protected PrefixAndSuffix keyPas;

	protected double previousMoney = DEFAULT_MONEY;

	protected Map<Object, Double> previousNumberMap = new HashMap<Object, Double>();

	protected Map<Object, Double> previousPriceMap = new HashMap<Object, Double>();

	protected Map<Object, Double> previousWeightMap = new HashMap<Object, Double>();

	private int index = 0;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		if (index == 0) {
			for (Map<String, Object> row : rowSet) {
				Double price = ((Number)pricePas.getValue(row)).doubleValue();
				Double weight = ((Number)weightPas.getValue(row)).doubleValue();
				Object key = keyPas.getValue(row);
				previousWeightMap.put(key, weight);
				previousNumberMap.put(key, previousMoney * weight / price);
				previousPriceMap.put(key, price);
			}
			index++;
			return getResult(previousMoney);
		} else {
			boolean weightChange = false;
			for (Map<String, Object> row : rowSet) {
				Number weight = weightPas.getValue(row);
				if (weight != null) {
					weightChange = true;
					break;
				}
			}
			double totalWeight = 0;
			double currentMoney = 0;
			for (Map<String, Object> row : rowSet) {
				Double price = ((Number)pricePas.getValue(row)).doubleValue();
				Object key = keyPas.getValue(row);
				if (weightChange) {
					Double weight = ((Number)weightPas.getValue(row)).doubleValue();
					totalWeight += weight;
					previousNumberMap.put(key, previousMoney * weight / previousPriceMap.get(key));
				} else {
					totalWeight += previousWeightMap.get(key);
					currentMoney += previousNumberMap.get(key) * price;
				}
			}
			double notUsedMoney = (1 - totalWeight) * previousMoney;
			if (weightChange) {
				// 权重有变化
				for (Map<String, Object> row : rowSet) {
					Double price = ((Number)pricePas.getValue(row)).doubleValue();
					Object key = keyPas.getValue(row);
					Double weight = ((Number)weightPas.getValue(row)).doubleValue();
					previousWeightMap.put(key, weight);
					previousPriceMap.put(key, price);
					currentMoney += previousNumberMap.get(key) * price;
				}
			}
			currentMoney += notUsedMoney;
			double result = getResult(currentMoney);
			previousMoney = currentMoney;
			index++;
			return result;
		}

	}

	protected double getResult(double currentMoney) {
		return (currentMoney - previousMoney) / previousMoney;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		this.pricePas = this.pasList.get(0);
		this.weightPas =this.pasList.get(1);
		this.keyPas = this.pasList.get(2);
	}

}

// $Id: YieldByWeight.java 13283 2010-05-27 01:00:48Z wei.zhang $