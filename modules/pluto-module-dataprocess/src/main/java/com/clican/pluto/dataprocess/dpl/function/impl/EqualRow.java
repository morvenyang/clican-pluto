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

/**
 * 判断如果某行满足第一个参数条件则返回该行，如果有多行都满足只返回第一行
 * <p>
 * equalRow(boolean condition,Object row)
 *
 * @author wei.zhang
 *
 */
public class EqualRow extends BaseMultiRowFunction {
	/**
	 * 函数参数
	 */
	private PrefixAndSuffix equalCondition;

	private PrefixAndSuffix equalRow;

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		if (params == null || params.size() != 2) {
			throw new DplParseException();
		}
		equalCondition = this.pasList.get(0);
		equalRow = this.pasList.get(1);
	}

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		for (Map<String, Object> row : rowSet) {
			Boolean result = equalCondition.getValue(row);
			if (result) {
				return equalRow.getValue(row);
			}
		}
		return null;
	}

}

// $Id: EqualRow.java 13268 2010-05-26 10:12:11Z wei.zhang $