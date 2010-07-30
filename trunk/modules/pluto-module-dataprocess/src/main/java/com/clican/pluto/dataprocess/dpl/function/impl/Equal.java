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
 * 判断2个对象是否相等,如果2个数值有任意一个为空则报错
 * <p>
 * equal(Object obj1,Object obj2)
 *
 * @author clican
 *
 */
public class Equal extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;

	
	public Object calculate(Map<String, Object> row) throws CalculationException,PrefixAndSuffixException {
		Object value1 = pas1.getValue(row);
		Object value2 = pas2.getValue(row);
		
		if (value1 == null || value2 == null) {
			throw new CalculationException("无法获得指定列的数据，列定义正确？");
		}
		return value1.equals(value2);
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

// $Id: Equal.java 13267 2010-05-26 10:10:28Z wei.zhang $