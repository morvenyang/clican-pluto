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

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 *
 * @author wei.zhang
 *
 */
public class Atr extends BaseMultiRowFunction {

	/**
	 * 交易数据的List
	 */
	private PrefixAndSuffix exchangePas;

	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		List<Double> exchangeList = new ArrayList<Double>();
		Double buy = 0d;
		Double sell = 0d;
		for (Map<String, Object> row : rowSet) {
			Double exchange = exchangePas.getValue(row);
			exchangeList.add(exchange);
			if (exchange > 0) {
				buy += exchange;
			} else {
				sell += (-exchange);
			}
		}
		Double result = Math.min(buy, sell) * 2 / (exchangeList.get(0) + exchangeList.get(exchangeList.size() - 1));
		return result;
	}

	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		if (params == null || params.size() != 1) {
			throw new DplParseException();
		}
		this.exchangePas = this.pasList.get(0);
	}
}

// $Id: Atr.java 13257 2010-05-26 09:57:57Z wei.zhang $