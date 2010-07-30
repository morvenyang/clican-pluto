/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author chulin.gui
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 离一个固定的日期是否相隔7天的周期
 * 
 * @author chulin.gui
 * 
 */
public class SevenDayPeriod extends BaseSingleRowFunction {
	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;
	private long period = 24 * 3600 * 1000;

	
	public Object calculate(Map<String, Object> row)
			throws CalculationException, PrefixAndSuffixException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date indexDate = sdf.parse("20050101");
			Date d1 = sdf.parse(pas1.getValue(row).toString());
			Date d2 = pas2 == null ? indexDate : sdf.parse(pas2.getValue(row)
					.toString());
			return ((d1.getTime() - d2.getTime()) / period) % 7 == 0 ? true
					: false;
		} catch (Exception e) {
			throw new CalculationException(e);
		}

	}

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		pas1 = this.pasList.get(0);
		if (this.pasList.size() == 2) {
			pas2 = this.pasList.get(1);
		}
	}

}

// $Id$