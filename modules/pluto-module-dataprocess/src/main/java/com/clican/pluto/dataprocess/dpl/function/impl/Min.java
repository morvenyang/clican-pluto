/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class Min extends BaseMultiRowFunction {

	/**
	 * 函数参数
	 */
	private PrefixAndSuffix prefixAndSuffix;

	@Override
	public void setParams(List<Object> params, From from,ProcessorContext context) throws DplParseException {
		super.setParams(params, from,context);
		if (params == null || params.size() != 1) {
			throw new DplParseException();
		}
		prefixAndSuffix = this.getPrefixeAndSuffix(params).get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		Comparable result = null;
		for (Map<String, Object> row : rowSet) {
			Comparable obj = prefixAndSuffix.getValue(row);
			if (obj == null) {
				continue;
			} else {
				if (result == null) {
					result = obj;
				} else {
					if (result.compareTo(obj) > 0) {
						result = obj;
					}
				}
			}
		}
		return result;
	}

}


//$Id: Min.java 12410 2010-05-13 06:55:57Z wei.zhang $