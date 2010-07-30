/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 重置权重
 *
 * @author clican
 *
 */
public class ResetWeight extends BaseSingleRowFunction {

	/**
	 * 当前的权重
	 */
	private PrefixAndSuffix weight;

	/**
	 * 如果时间和前一个时间一样则返回null
	 */
	private PrefixAndSuffix date;

	/**
	 * 用来记录前一条记录的时间
	 */
	private String previousDate;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Double w = weight.getValue(row);
		String d = date.getValue(row);
		if (previousDate == null) {
			previousDate = d;
			return w;
		} else {
			if (previousDate.equals(d)) {
				return null;
			} else {
				previousDate = d;
				return w;
			}
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		this.weight = this.pasList.get(0);
		this.date = this.pasList.get(1);
		if (this.pasList.size() > 2) {
			previousDate = pasList.get(2).getConstantsValue(String.class);
		}
	}

	
	public boolean isLazyCalc() {
		return false;
	}

}

// $Id: ResetWeight.java 16257 2010-07-16 09:17:22Z wei.zhang $