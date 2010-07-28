/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 * @author wei.zhang
 * 
 */
public class YieldByExchange extends MoneyByExchange {

	private Double previousMoney;

	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		if (previousMoney == null) {
			return 0d;
		}
		Double money = (Double) super.calculate(rowSet);
		Double yield = (money - previousMoney) / previousMoney;
		this.previousMoney = money;
		return yield;
	}

}

// $Id: YieldByExchange.java 12410 2010-05-13 06:55:57Z wei.zhang $