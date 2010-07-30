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

import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 * 日期加减函数 
 * <p>
 * addDate(Date date,int field,int add)
 * 
 * @author clican
 * 
 */
public class AddDate extends BaseSingleRowFunction {

	private PrefixAndSuffix date;

	private int add;

	private int field;

	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Date d = date.getValue(row);
		return DateUtils.add(d, field, add);
	}

	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		this.date = this.pasList.get(0);
		String addField = this.pasList.get(1).getConstantsValue();
		if (addField.endsWith("day")) {
			field = Calendar.DAY_OF_MONTH;
			add = Integer.parseInt(addField.replaceAll("day", ""));
		} else if (addField.endsWith("month")) {
			field = Calendar.MONTH;
			add = Integer.parseInt(addField.replaceAll("month", ""));
		} else if (addField.endsWith("year")) {
			field = Calendar.YEAR;
			add = Integer.parseInt(addField.replaceAll("year", ""));
		} else {
			throw new PrefixAndSuffixException("不支持的日期步长类型");
		}
	}

}

// $Id: AddDate.java 13255 2010-05-26 09:52:13Z wei.zhang $