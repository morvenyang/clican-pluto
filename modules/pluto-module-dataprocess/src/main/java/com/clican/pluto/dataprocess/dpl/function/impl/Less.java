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
 * 对2个可比较数据进行大小比较，如果参数1小于参数2则返回True
 * 
 * <p>
 * less(Comparable c1,Comparable c2)
 * 
 * @author clican
 * 
 */
public class Less extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;

	@SuppressWarnings("unchecked")
	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Comparable value1 = pas1.getValue(row);
		Comparable value2 = pas2.getValue(row);
		if (value1 instanceof Number && value2 instanceof Number) {
			return ((Number) value1).doubleValue() < ((Number) value2).doubleValue();
		} else {
			return value1.compareTo(value2) < 0;
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		pas1 = this.pasList.get(0);
		pas2 = this.pasList.get(1);
	}
}

// $Id: Less.java 13291 2010-05-27 01:10:53Z wei.zhang $