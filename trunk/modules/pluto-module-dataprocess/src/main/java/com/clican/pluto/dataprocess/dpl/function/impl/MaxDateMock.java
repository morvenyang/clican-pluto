/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class MaxDateMock extends BaseSingleRowFunction {
	
	private PrefixAndSuffix date;

	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException,PrefixAndSuffixException {
		return maxDateMock((Date) date.getValue(row));
	}

	@Override
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		date = this.pasList.get(0);
	}
	
	public static Date maxDateMock(Date d) throws CalculationException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (d == null) {
			try {
				return sdf.parse("9999-12-31");
			} catch (ParseException e) {
				throw new CalculationException(e);
			}
		} else {
			return d;
		}
	}
}

// $Id: MaxDateMock.java 12410 2010-05-13 06:55:57Z wei.zhang $