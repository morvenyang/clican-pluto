/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 字符串拼接函数
 * <p>
 * append(String s1,String s2,String s3...)
 * @author clican
 *
 */
public class Append extends BaseSingleRowFunction {

	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		String result = "";
		for (int i = 0; i < pasList.size(); i++) {
			Object value = pasList.get(i).getValue(row);
			if (value != null) {
				if (value instanceof Double) {
					result += ((Double) value).doubleValue() + "";
				} else {
					result += value.toString();
				}

			}
		}
		return result;
	}

	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
	}

}

// $Id: Append.java 13256 2010-05-26 09:53:01Z wei.zhang $