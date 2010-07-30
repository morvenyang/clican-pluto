/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 比较特殊,考虑到银行结转日期是每个季度的第一个月的21日
 * 
 * @author clican
 * 
 */
public class DayOfQuarter extends BaseSingleRowFunction {

	private PrefixAndSuffix date;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Date d = date.getValue(row);
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		String s = sdf.format(d);
		if (s.compareTo("0321") < 0) {
			// 返回12 1 2的天数
			if (IsLeapYear.isLeapYear(d)) {
				return 91;
			} else {
				return 90;
			}
		} else if (s.compareTo("1221") >= 0) {
			// 返回12 1 2的天数
			if (IsLeapYear.isLeapYear(DateUtils.addYears(d, 1))) {
				return 91;
			} else {
				return 90;
			}
		} else if (s.compareTo("0921") >= 0) {
			// 返回9 10 11的天数
			return 91;
		} else if (s.compareTo("0621") >= 0) {
			// 返回6 7 8的天数
			return 92;
		} else {
			// 返回3 4 5的天数
			return 92;
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		this.date = pasList.get(0);
	}

}

// $Id: DayOfQuarter.java 12410 2010-05-13 06:55:57Z wei.zhang $