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
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class YieldByMapWeight extends BaseSingleRowFunction {

	public final static double DEFAULT_MONEY = 1000d;

	private PrefixAndSuffix priceMapPas;

	private PrefixAndSuffix weightMapPas;

	protected Map<Object, Double> previousNumberMap = new HashMap<Object, Double>();

	protected Map<Object, Double> previousPriceMap = new HashMap<Object, Double>();

	protected Map<Object, Double> previousWeightMap = new HashMap<Object, Double>();

	private int index = 0;

	protected double previousMoney = DEFAULT_MONEY;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Map<String, Double> priceMap = priceMapPas.getValue(row);
		Map<String, Double> weightMap = weightMapPas.getValue(row);
		if (index == 0) {
			for (String key : priceMap.keySet()) {
				Double price = priceMap.get(key);
				Double weight = weightMap.get(key);
				previousWeightMap.put(key, weight);
				previousNumberMap.put(key, previousMoney * weight / price);
				previousPriceMap.put(key, price);
			}
			index++;
			return getResult(previousMoney);
		} else {
			boolean weightChange = true;
			if (weightMap.size() == 0) {
				weightChange = false;
			}
			for (String key : priceMap.keySet()) {
				if (weightMap.get(key) == null) {
					weightChange = false;
					break;
				}
			}
			double totalWeight = 0;
			double currentMoney = 0;
			for (String key : priceMap.keySet()) {
				Double price = priceMap.get(key);
				Double weight = weightMap.get(key);
				if (weightChange) {
					totalWeight += weight;
					if (previousPriceMap.containsKey(key)) {
						previousNumberMap.put(key, previousMoney * weight / previousPriceMap.get(key));
					} else {
						previousNumberMap.put(key, previousMoney * weight / price);
					}
				} else {
					totalWeight += previousWeightMap.get(key);
					currentMoney += previousNumberMap.get(key) * price;
					previousPriceMap.put(key, price);
				}
			}

			double notUsedMoney = (1 - totalWeight) * previousMoney;
			if (weightChange) {
				// 权重有变化
				for (String key : priceMap.keySet()) {
					Double price = priceMap.get(key);
					Double weight = weightMap.get(key);
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
		this.priceMapPas = this.pasList.get(0);
		this.weightMapPas = this.pasList.get(1);
	}
}

// $Id$