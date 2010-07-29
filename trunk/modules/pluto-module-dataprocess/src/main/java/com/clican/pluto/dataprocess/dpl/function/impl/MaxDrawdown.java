/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算最大最大Drawdown指标指标
 * 
 * @author wei.zhang
 * 
 */
public class MaxDrawdown extends BaseMultiRowFunction {

	private PrefixAndSuffix ratioPas;

	private PrefixAndSuffix datePas;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		List<Double> ratioList = new ArrayList<Double>();
		List<Date> dateList = new ArrayList<Date>();
		for (Map<String, Object> row : rowSet) {
			Double ratio = ratioPas.getValue(row);
			Date date = datePas.getValue(row);
			ratioList.add(ratio);
			dateList.add(date);
		}
		Date start = null;
		Date end = null;
		Double maxSumRatio = 0.0d;
		for (int i = 0; i < ratioList.size(); i++) {
			Double ratio1 = ratioList.get(i);
			if (ratio1 < maxSumRatio) {
				maxSumRatio = ratio1;
				end = dateList.get(i);
				start = end;
			}
			for (int j = i - 1; j >= 0; j--) {
				Double ratio2 = ratioList.get(j);
				ratio1 = (ratio1 + 1) * (ratio2 + 1) - 1;
				if (ratio1 < maxSumRatio) {
					maxSumRatio = ratio1;
					end = dateList.get(i);
					start = dateList.get(j);
				} 
			}
		}
		log.debug("start=[" + start + "]end=[" + end + "]");
		return maxSumRatio;
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.ratioPas = this.pasList.get(0);
		this.datePas = this.pasList.get(1);
	}

}

// $Id$