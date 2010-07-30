/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class MinDateMock extends BaseSingleRowFunction {


	private PrefixAndSuffix date;

	
	public Object calculate(Map<String, Object> row) throws CalculationException ,PrefixAndSuffixException{
		return minDateMock((Date) date.getValue(row));
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		date = this.pasList.get(0);
	}

	public static Date minDateMock(Date d) throws CalculationException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (d == null) {
			try {
				return sdf.parse("0001-01-01");
			} catch (ParseException e) {
				throw new CalculationException(e);
			}
		} else {
			return d;
		}
	}
}


//$Id: MinDateMock.java 12410 2010-05-13 06:55:57Z wei.zhang $