/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 判断当前年是否是润年
 * 
 * <p>
 * isLeapYear(Date date)
 *
 * @author clican
 *
 */
public class IsLeapYear extends BaseSingleRowFunction {

	private PrefixAndSuffix date;

	
	public Object calculate(Map<String, Object> row) throws CalculationException ,PrefixAndSuffixException{
		Date d = date.getValue(row);
		return isLeapYear(d);
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		this.date = this.pasList.get(0);
	}

	public static boolean isLeapYear(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int year = c.get(Calendar.YEAR);
		return (year % 4 == 0 && year % 100 != 0)||year % 400 == 0;
	}
}

// $Id: IsLeapYear.java 13287 2010-05-27 01:05:39Z wei.zhang $