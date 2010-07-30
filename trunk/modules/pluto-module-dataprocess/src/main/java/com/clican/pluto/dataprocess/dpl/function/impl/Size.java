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
 * 计算List的长度
 *
 * @author clican
 *
 */
public class Size extends BaseSingleRowFunction {

	private PrefixAndSuffix dataList;

	
	public Object calculate(Map<String, Object> row) throws CalculationException,PrefixAndSuffixException {
		Object obj = dataList.getValue(row);
		if (obj instanceof List) {
			return ((List<?>) obj).size();
		} else {
			throw new CalculationException("Size函数只支持传入的参数是一个List");
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		dataList = this.pasList.get(0);
	}

}

// $Id: Size.java 16261 2010-07-16 09:20:39Z wei.zhang $