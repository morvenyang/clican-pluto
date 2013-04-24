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

public class Count extends BaseMultiRowFunction {

	private PrefixAndSuffix valuePas;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException {
		int size = 0;
		for (Map<String, Object> row : rowSet) {
			Object value = valuePas.getValue(row);
			if (value != null) {
				size++;
			}
		}
		return size;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		this.valuePas = this.pasList.get(0);
	}

}

// $Id$