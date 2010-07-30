/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 根据枚举的权重来计算复合收益率,
 * 如果某行的weight为空的话则说明权重没有再平衡
 * <p>
 * yieldByEnumWeight(List<Double> prices1,List<Double> weights1,String key1,List<Double> prices2,List<Double> weights2,String key2,...)
 *
 * @author wei.zhang
 *
 */
public class YieldByEnumWeight extends BaseSingleRowFunction {

	public final static double DEFAULT_MONEY = 1000d;
	/**
	 * 函数参数
	 */
	protected List<PrefixAndSuffix> pricePasList = new ArrayList<PrefixAndSuffix>();
	/**
	 * 函数参数
	 */
	protected List<PrefixAndSuffix> weightPasList = new ArrayList<PrefixAndSuffix>();
	/**
	 * 函数参数
	 */
	protected List<PrefixAndSuffix> keyPasList = new ArrayList<PrefixAndSuffix>();

	protected double previousMoney = DEFAULT_MONEY;

	protected Map<Object, Double> previousNumberMap = new HashMap<Object, Double>();

	protected Map<Object, Double> previousPriceMap = new HashMap<Object, Double>();

	protected Map<Object, Double> previousWeightMap = new HashMap<Object, Double>();

	private int index = 0;

	private int enumNum = 0;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		if (index == 0) {
			for (int i = 0; i < enumNum; i++) {
				PrefixAndSuffix pricePas = pricePasList.get(i);
				PrefixAndSuffix weightPas = weightPasList.get(i);
				PrefixAndSuffix keyPas = keyPasList.get(i);
				Double price = ((Number) pricePas.getValue(row)).doubleValue();
				Double weight = ((Number) weightPas.getValue(row)).doubleValue();
				Object key = keyPas.getValue(row);
				previousWeightMap.put(key, weight);
				previousNumberMap.put(key, previousMoney * weight / price);
				previousPriceMap.put(key, price);
			}
			index++;
			return getResult(previousMoney);
		} else {
			boolean weightChange = false;
			for (int i = 0; i < enumNum; i++) {
				PrefixAndSuffix weightPas = weightPasList.get(i);
				Number numWeight = ((Number) weightPas.getValue(row));
				if (numWeight != null) {
					weightChange = true;
					break;
				}
			}

			double totalWeight = 0;
			double currentMoney = 0;
			for (int i = 0; i < enumNum; i++) {
				PrefixAndSuffix pricePas = pricePasList.get(i);
				PrefixAndSuffix weightPas = weightPasList.get(i);
				PrefixAndSuffix keyPas = keyPasList.get(i);
				Double price = ((Number) pricePas.getValue(row)).doubleValue();
				Object key = keyPas.getValue(row);
				if (weightChange) {
					Double weight = ((Number) weightPas.getValue(row)).doubleValue();
					totalWeight += weight;
					previousNumberMap.put(key, previousMoney * weight / previousPriceMap.get(key));
				} else {
					totalWeight += previousWeightMap.get(key);
					currentMoney += previousNumberMap.get(key) * price;
					previousPriceMap.put(key, price);
				}
			}

			double notUsedMoney = (1 - totalWeight) * previousMoney;
			if (weightChange) {
				// 权重有变化
				for (int i = 0; i < enumNum; i++) {
					PrefixAndSuffix pricePas = pricePasList.get(i);
					PrefixAndSuffix weightPas = weightPasList.get(i);
					PrefixAndSuffix keyPas = keyPasList.get(i);
					Double price = ((Number) pricePas.getValue(row)).doubleValue();
					Object key = keyPas.getValue(row);
					Double weight = ((Number) weightPas.getValue(row)).doubleValue();
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

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	protected double getResult(double currentMoney) {
		return (currentMoney - previousMoney) / previousMoney;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		for (int i = 0; i < pasList.size() / 3 * 3; i = i + 3) {
			this.pricePasList.add(pasList.get(i));
			this.weightPasList.add(pasList.get(i + 1));
			this.keyPasList.add(pasList.get(i + 2));
			enumNum++;
		}
	}
}

// $Id: YieldByEnumWeight.java 13271 2010-05-26 10:25:08Z wei.zhang $