/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.function.MultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class TotalVolume extends BaseFunction implements SingleRowFunction {

	private PrefixAndSuffix amount;

	private PrefixAndSuffix ptv;

	private PrefixAndSuffix convertDate;

	private Double previousTotalVolume;

	private double sumAmount = 0;

	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		throw new CalculationException("This method shall never be invoked");
	}

	@Override
	public boolean containMultiRowCalculation() {
		for (PrefixAndSuffix pas : this.pasList) {
			if (pas.getFunction() instanceof MultiRowFunction) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object recurseCalculate(List<Map<String, Object>> rowSet, Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		if (previousTotalVolume == null) {
			previousTotalVolume = ((Number) ptv.getValue(row)).doubleValue();
			if (previousTotalVolume == null) {
				throw new CalculationException("没有前一个TotalVolume无法计算当前的TotalVolume");
			}
		}
		Double amo = amount.getValue(row);
		Number cd = convertDate.getValue(row);
		if (amo == null || cd == null) {
			Date date = (Date) row.get("dateList");
			log.warn("万份收益为空,这个肯定是由于该天[" + date + "]数据缺失造成的原因.目前把该天的万份收益设置为0");
			amo = 0d;
			cd = new Integer(0);
		}
		sumAmount += amo;
		double totalVolume = previousTotalVolume * (1 + sumAmount / 10000);

		if (cd.intValue() == 1) {
			previousTotalVolume = totalVolume;
			sumAmount = 0;
		}
		return totalVolume;
	}

	@Override
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.amount = this.pasList.get(0);
		this.ptv = this.pasList.get(1);
		this.convertDate = this.pasList.get(2);
	}

}

// $Id: TotalVolume.java 16346 2010-07-21 05:27:30Z wei.zhang $