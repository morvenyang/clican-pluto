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

/**
 * 对2个可比较数据进行大小比较，如果参数1大于参数2则返回True
 * 
 * <p>
 * great(Comparable c1,Comparable c2)
 *
 * @author wei.zhang
 *
 */
public class Great extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;

	@SuppressWarnings("unchecked")
	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Comparable value1 = pas1.getValue(row);
		Comparable value2 = pas2.getValue(row);
		if (value1 instanceof Number && value2 instanceof Number) {
			return ((Number) value1).doubleValue() > ((Number) value2).doubleValue();
		} else {
			return value1.compareTo(value2) > 0;
		}
	}

	@Override
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		pas1 = this.pasList.get(0);
		pas2 = this.pasList.get(1);
	}

}

// $Id: Great.java 13292 2010-05-27 01:11:17Z wei.zhang $