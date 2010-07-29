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
 * 
 * 根据各自的权重来计算复合指数,计算参数定义参见
 * 
 * @see YieldByWeight
 * 
 * @author wei.zhang
 * 
 */
public class IndexByWeight extends YieldByWeight {

	
	protected double getResult(double currentMoney) {
		return currentMoney;
	}

	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		Number temp = (Number) this.pasList.get(3).getConstantsValue();
		if (temp != null) {
			this.previousMoney = temp.doubleValue();
		}

	}
}

// $Id: IndexByWeight.java 13284 2010-05-27 01:02:04Z wei.zhang $