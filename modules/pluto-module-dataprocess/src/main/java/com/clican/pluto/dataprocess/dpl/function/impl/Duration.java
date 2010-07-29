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

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 * 计算两个时间的天数之差，参数一为终值，参数二为始值。
 * <p>
 * step的默认值是'day'
 * duration(Date end,Date start,String step)
 * 
 * @author wei.zhang
 * 
 */
public class Duration extends BaseSingleRowFunction {

	private PrefixAndSuffix date1;

	private PrefixAndSuffix date2;

	private PrefixAndSuffix step;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Date d1 = date1.getValue(row);// 终值
		Date d2 = date2.getValue(row);// 始值
		if (step != null) {
			return duration(d1, d2, (String) step.getValue(row));
		} else {
			return duration(d1, d2, "day");
		}

	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		date1 = this.pasList.get(0);
		date2 = this.pasList.get(1);
		if (this.pasList.size() > 2) {
			step = this.pasList.get(2);
		}
	}

	public static double duration(Date d1, Date d2, String step) throws PrefixAndSuffixException {
		if (StringUtils.isEmpty(step) || step.equals("day")) {
			return (d1.getTime() - d2.getTime()) / (1000L * 3600 * 24);
		} else if (step.equals("month")) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(d1);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(d2);
			int month = (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12 + (c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH));
			return month;
		} else {
			throw new PrefixAndSuffixException("不支持的步长类型");
		}
	}

	public static double duration(Date d1, Date d2, boolean closeEnd) throws PrefixAndSuffixException {
		if (closeEnd) {
			return duration(d1, d2, "day") + 1;
		} else {
			return duration(d1, d2, "day");
		}
	}

	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		System.out.println(duration(sdf.parse("20070630"), sdf.parse("20070101"), true));
		System.out.println(duration(sdf.parse("20071231"), sdf.parse("20070701"), true));
		System.out.println(duration(sdf.parse("20071231"), sdf.parse("20070101"), true));
		System.out.println(duration(sdf.parse("20080630"), sdf.parse("20080101"), true));
		System.out.println(duration(sdf.parse("20081231"), sdf.parse("20080701"), true));
		System.out.println(duration(sdf.parse("20081231"), sdf.parse("20080101"), true));
		System.out.println(duration(sdf.parse("20090630"), sdf.parse("20090101"), true));
		System.out.println(duration(sdf.parse("20091231"), sdf.parse("20090701"), true));
		System.out.println(duration(sdf.parse("20091231"), sdf.parse("20090101"), true));
	}
}

// $Id: Duration.java 13263 2010-05-26 10:07:19Z wei.zhang $