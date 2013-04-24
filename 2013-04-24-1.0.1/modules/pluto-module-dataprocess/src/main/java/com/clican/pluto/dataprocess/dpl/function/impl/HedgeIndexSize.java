/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author chulin.gui
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class HedgeIndexSize extends BaseMultiRowFunction {

	private PrefixAndSuffix ratioPas;

	private PrefixAndSuffix tickerPas;

	private List<Set<String>> previousTickerSet = new ArrayList<Set<String>>();

	private Map<String, Integer> usingTickerMap = new HashMap<String, Integer>();
	
	private Map<String, Double> usingRatioMap = new HashMap<String, Double>();

	private boolean beginCalc = false;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		if (rowSet.size() == 0) {
			if (beginCalc) {
				previousTickerSet.add(new HashSet<String>());
				// 如果连续4周没有点则从usingTickerSet中剔除
				for (String ticker : new ArrayList<String>(usingTickerMap.keySet())) {
					Integer count = usingTickerMap.get(ticker);
					if (count >= 3) {
						usingTickerMap.remove(ticker);
						usingRatioMap.remove(ticker);
					} else {
						usingTickerMap.put(ticker, count + 1);
					}
				}
			} else {
				//还未开始计算直接返回null
				return null;
			}
		} else {
			beginCalc = true;
			Map<String,Double> ratioMap = new HashMap<String,Double>();
			for(Map<String,Object> row:rowSet){
				String ticker = tickerPas.getValue(row);
				Double ratio = ((Number)ratioPas.getValue(row)).doubleValue();
				ratioMap.put(ticker, ratio);
			}
			Set<String> tickerSet = new HashSet<String>(ratioMap.keySet());
			previousTickerSet.add(tickerSet);
			//处理3个月(即12周)内满9个点
			for (String ticker : tickerSet) {
				//用新的ratio覆盖原来旧的
				if (usingTickerMap.keySet().contains(ticker)) {
					usingTickerMap.put(ticker, 0);
					usingRatioMap.put(ticker, ratioMap.get(ticker));
					continue;
				}
				int count = 0;
				int continuedNotFoundCount = 0;
				for (int i = previousTickerSet.size() - 1; i >= previousTickerSet.size() - 13 && i > 0; i--) {
					Set<String> preSet = previousTickerSet.get(i);
					if (preSet.contains(ticker)) {
						count++;
						continuedNotFoundCount = 0;
					} else {
						continuedNotFoundCount++;
					}
				}
				//不计算上本周的点所以是8个点
				if (count >= 8 && continuedNotFoundCount < 4) {
					usingTickerMap.put(ticker, 0);
					usingRatioMap.put(ticker, 0d);
				}
			}
			//判断那些原来使用的ticker需要移除的
			for (String ticker : new ArrayList<String>(usingTickerMap.keySet())) {
				if (tickerSet.contains(ticker)) {
					usingTickerMap.put(ticker, 0);
				} else {
					Integer count = usingTickerMap.get(ticker);
					if (count >= 3) {
						usingTickerMap.remove(ticker);
						usingRatioMap.remove(ticker);
					} else {
						usingTickerMap.put(ticker, count + 1);
					}
				}
			}

		}
		Double sumRatio = 0d;

		for (Double ratio: usingRatioMap.values()) {
			sumRatio += ratio;
		}
		int size;
		if (usingRatioMap.size() > 0) {
			size = usingRatioMap.size();
		} else {
			size = 0;
		}
		
		return size;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		this.ratioPas = this.pasList.get(0);
		this.tickerPas = this.pasList.get(1);
	}

}


//$Id$