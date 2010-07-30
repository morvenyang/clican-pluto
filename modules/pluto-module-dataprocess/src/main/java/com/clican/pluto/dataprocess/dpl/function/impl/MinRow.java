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
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class MinRow extends BaseMultiRowFunction {

	/**
	 * 函数参数
	 */
	private PrefixAndSuffix minCondition;
	
	private PrefixAndSuffix minRow;
	
	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		if (params == null || params.size() != 2) {
			throw new DplParseException();
		}
		minCondition = this.getPrefixeAndSuffix(params).get(0);
		minRow = this.getPrefixeAndSuffix(params).get(1);
	}
	@SuppressWarnings("unchecked")
	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		Object rowObj = null;
		Comparable result = null;
		for (Map<String, Object> row : rowSet) {
			Comparable obj = minCondition.getValue(row);
			if (obj == null) {
				continue;
			} else {
				if (result == null) {
					result = obj;
					rowObj = minRow.getValue(row);
				} else {
					if (result.compareTo(obj) > 0) {
						result = obj;
						rowObj = minRow.getValue(row);
					}
				}
			}
		}
		return rowObj;
	}

}


//$Id$