/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class MatchPriceAndWeight extends BaseMultiRowFunction {

	private PrefixAndSuffix datePas;

	private PrefixAndSuffix priceCodePas;

	private PrefixAndSuffix pricePas;

	private PrefixAndSuffix weightCodePas;

	private PrefixAndSuffix weightPas;

	private PrefixAndSuffix resetDurationPas;

	private List<String> allCodes;

	private Map<String, Double> previousWeightMap = new HashMap<String, Double>();

	private String previousResetDuration = null;

	private Date previousDate;

	private Map<String, Double> weightMap = new HashMap<String, Double>();

	@SuppressWarnings("unchecked")
	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		Map<String, Object> mapRow = new HashMap<String, Object>();
		boolean clear = true;
		Date date = null;
		for (Map<String, Object> row : rowSet) {
			date = datePas.getValue(row);
			String weightCode = weightCodePas.getValue(row);
			Number weight = weightPas.getValue(row);
			if (StringUtils.isNotEmpty(weightCode) && clear) {
				previousWeightMap.clear();
				previousResetDuration = resetDurationPas.getValue(row);
				previousDate = datePas.getValue(row);
				clear = false;
			}
			if (StringUtils.isNotEmpty(weightCode)) {
				previousWeightMap.put(weightCode, weight.doubleValue());
			}
		}
		// 权重被重新大的调整过了
		boolean changeWeight = !clear;
		if (changeWeight) {
			for (String code : allCodes) {
				if (!previousWeightMap.containsKey(code)) {
					previousWeightMap.put(code, 0d);
				}
			}
			weightMap = new HashMap<String, Double>(previousWeightMap);
		} else {
			if (StringUtils.isNotEmpty(previousResetDuration)) {
				if (previousResetDuration.equals("1D")) {
					weightMap = new HashMap<String, Double>(previousWeightMap);
				} else if (previousResetDuration.equals("1W")) {
					if (!ToChar.toYearAndWeek(previousDate).equals(ToChar.toYearAndWeek(date))) {
						previousDate = date;
						weightMap = new HashMap<String, Double>(previousWeightMap);
					}
				} else if (previousResetDuration.equals("1M")) {
					Date temp = DateUtils.addMonths(previousDate,  1);
					if (temp.compareTo(date) <= 0) {
						previousDate = date;
						weightMap = new HashMap<String, Double>(previousWeightMap);
					}
				} else if (previousResetDuration.equals("3M")) {
					Date temp = DateUtils.addMonths(previousDate, 3);
					if (temp.compareTo(date) <= 0) {
						previousDate = date;
						weightMap = new HashMap<String, Double>(previousWeightMap);
					}
				}
			}
		}

		Set<String> populatePriceSet = new HashSet<String>();
		for (Map<String, Object> row : rowSet) {
			mapRow.put("date", date);
			// 从来未设置过权重
			if (weightMap.size() == 0) {
				break;
			}
			String priceCode = priceCodePas.getValue(row);
			if (weightMap.containsKey(priceCode)) {
				populatePriceSet.add(priceCode);
			}
		}
		mapRow.put("date", date);
		if (populatePriceSet.size() == weightMap.size()) {
			for (Map<String, Object> row : rowSet) {
				String priceCode = priceCodePas.getValue(row);
				Number price = pricePas.getValue(row);
				if (StringUtils.isNotEmpty(priceCode) && weightMap.containsKey(priceCode)) {
					if (!mapRow.containsKey("priceMap")) {
						mapRow.put("priceMap", new HashMap<String, Double>());
					}
					((Map) mapRow.get("priceMap")).put(priceCode, price.doubleValue());
					if (!mapRow.containsKey("weightMap")) {
						mapRow.put("weightMap", new HashMap<String, Double>());
					}
					if (!((Map) mapRow.get("weightMap")).containsKey(priceCode)) {
						((Map) mapRow.get("weightMap")).put(priceCode, weightMap.get(priceCode));
					}
					if (previousResetDuration.equals("1W") || previousResetDuration.equals("1M") || previousResetDuration.equals("3M")) {
						// 接来的权重不用重置所以设置为空
						weightMap.put(priceCode, null);
					}
				}
			}
		}

		return mapRow;
	}

	@SuppressWarnings("unchecked")
	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.datePas = this.pasList.get(0);
		this.priceCodePas = this.pasList.get(1);
		this.pricePas = this.pasList.get(2);
		this.weightCodePas = this.pasList.get(3);
		this.weightPas = this.pasList.get(4);
		this.resetDurationPas = this.pasList.get(5);
		this.allCodes = (List) this.pasList.get(6).getConstantsValue();
	}

}

// $Id$