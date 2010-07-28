/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

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

/**
 * 
 * 把字符串格式的日期转换为日期对象
 * 
 * 该函数参数描述
 * 
 * 
 * toDate(string date,string patten)
 * 
 * date - 字符串格式的日期 patten - 日期格式可以不提供默认就是yyyyMMdd HH:mm:ss
 * 
 * 
 * @author wei.zhang
 * 
 */
public class ToDate extends BaseSingleRowFunction {
	/**
	 * 函数参数
	 */
	private PrefixAndSuffix valuePas;
	/**
	 * 函数参数
	 */
	private SimpleDateFormat patten;

	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException,PrefixAndSuffixException {
		Object obj = valuePas.getValue(row);
		if (obj instanceof String) {
			Date date = null;
			try {
				date = patten.parse((String) obj);
			} catch (Exception e) {
				throw new CalculationException("日期格式错误", e);
			}
			return date;
		} else if (obj != null) {
			Date date = null;
			try {
				date = patten.parse(obj.toString());
			} catch (Exception e) {
				throw new CalculationException("日期格式错误", e);
			}
			return date;
		} else {
			throw new CalculationException("日期必须从字符串转换获得，现在源数据不是字符串");
		}
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		valuePas = this.pasList.get(0);
		if (params.size() == 2) {
			patten = new SimpleDateFormat((String)this.pasList.get(1).getConstantsValue());
		} else {
			patten = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		}
	}

	@Override
	public boolean isSupportWhere() {
		return true;
	}

}

// $Id: ToDate.java 12410 2010-05-13 06:55:57Z wei.zhang $