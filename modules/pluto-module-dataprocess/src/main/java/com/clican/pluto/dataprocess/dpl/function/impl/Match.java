/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 用Java的正则表达式进行Match比较
 * <p>
 * 
 * match(String value,String regex)
 * 
 * @author clican
 * 
 */
public class Match extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		String value1 = pas1.getValue(row);
		String value2 = pas2.getValue(row);
		if (StringUtils.isEmpty(value2)) {
			return false;
		}
		return value1.matches(value2);
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		pas1 = this.pasList.get(0);
		pas2 = this.pasList.get(1);
	}

}

// $Id: Match.java 13293 2010-05-27 01:19:18Z wei.zhang $