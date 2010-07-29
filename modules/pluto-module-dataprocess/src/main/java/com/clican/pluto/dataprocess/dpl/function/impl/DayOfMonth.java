/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 * 计算该日期所在的月份的天数,已经考虑了闰年和闰年
 * 
 * dayOfMonth(Date date)
 *
 * @author wei.zhang
 *
 */
public class DayOfMonth extends BaseSingleRowFunction {

	private PrefixAndSuffix date;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Date d = date.getValue(row);
		d = DateUtils.truncate(d, Calendar.MONTH);
		d = DateUtils.addMonths(d,  1);
		d = DateUtils.addDays(d,  -1);
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return Integer.parseInt(sdf.format(d));
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.date = pasList.get(0);
	}

}

// $Id: DayOfMonth.java 13260 2010-05-26 10:04:06Z wei.zhang $