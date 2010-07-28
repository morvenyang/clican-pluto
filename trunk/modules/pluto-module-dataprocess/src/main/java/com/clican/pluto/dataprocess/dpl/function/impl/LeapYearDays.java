/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author jerry.tian
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 返回该年的天数
 * <p>
 * leapYearDays(Date date)
 * 
 * @author wei.zhang
 * 
 */
public class LeapYearDays extends BaseSingleRowFunction {

	private PrefixAndSuffix date;

	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Date d = date.getValue(row);
		return IsLeapYear.isLeapYear(d) ? 366D : 365D;
	}

	@Override
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.date = this.pasList.get(0);
	}
}

// $Id: LeapYearDays.java 13289 2010-05-27 01:06:55Z wei.zhang $