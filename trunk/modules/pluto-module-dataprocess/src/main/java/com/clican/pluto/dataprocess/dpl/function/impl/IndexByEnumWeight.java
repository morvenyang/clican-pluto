/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;

import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 根据枚举的权重来计算复合指数,
 * 如果某行的weight为空的话则说明权重没有再平衡
 * <p>
 * indexByEnumWeight(List<Double> prices1,List<Double> weights1,String key1,List<Double> prices2,List<Double> weights2,String key2,...)
 *
 * @author wei.zhang
 *
 */
public class IndexByEnumWeight extends YieldByEnumWeight {

	
	protected double getResult(double currentMoney) {
		return currentMoney;
	}

	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		Number temp = (Number) this.pasList.get(pasList.size()-1).getConstantsValue();
		if (temp != null) {
			this.previousMoney = temp.doubleValue();
		}

	}
}

// $Id: IndexByEnumWeight.java 13272 2010-05-26 10:25:36Z wei.zhang $