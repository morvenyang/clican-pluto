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
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class Max extends BaseMultiRowFunction {
	/**
	 * 函数参数
	 */
	private PrefixAndSuffix prefixAndSuffix;

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		if (params == null || params.size() != 1) {
			throw new DplParseException();
		}
		prefixAndSuffix = this.pasList.get(0);
	}

	@SuppressWarnings("unchecked")
	
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
					if (result.compareTo(obj) < 0) {
						result = obj;
					}
				}
			}
		}
		return result;
	}
}

// $Id: Max.java 12410 2010-05-13 06:55:57Z wei.zhang $