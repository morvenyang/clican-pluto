/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author chulin.gui
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class YearPeriod extends BaseSingleRowFunction {

	private PrefixAndSuffix pas1;
	private PrefixAndSuffix pas2;
	private Integer period = 2;
	private Calendar c1 = Calendar.getInstance();
	private Calendar c2 = Calendar.getInstance();

	
	public Object calculate(Map<String, Object> row)
			throws CalculationException, PrefixAndSuffixException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {
			Date indexDate = sdf.parse("20050101");
			Date d1 = pas1.getValue(row);
			Date d2 = pas2 == null ? indexDate : sdf.parse(pas2.getValue(row)
					.toString());
			c1.setTime(d1);
			c2.setTime(d2);
			boolean retVal = c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
					&& c1.get(Calendar.DAY_OF_MONTH) == c2
							.get(Calendar.DAY_OF_MONTH)
					&& ((c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR))
							% period == 0);
			return retVal;
		} catch (Exception e) {
			throw new CalculationException(e);
		}

	}

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	
	public void setParams(List<Object> params, From from,
			ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		pas1 = this.pasList.get(0);
		if (this.pasList.size() == 2) {
			period = this.pasList.get(1).getConstantsValue(Integer.class);
		} else if (this.pasList.size() == 3) {
			pas2 = this.pasList.get(1);
			period = this.pasList.get(2).getConstantsValue(Integer.class);
		}
	}
}

// $Id$