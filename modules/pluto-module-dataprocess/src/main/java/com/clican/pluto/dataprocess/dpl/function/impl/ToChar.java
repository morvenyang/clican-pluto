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

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 * 把毫秒或日期格式的日期转换为字符串格式的日期
 * 
 * 该函数参数描述
 * 
 * 
 * toChar(date date,string patten)
 * 
 * date - 日期，格式可以是Long类型的millisecond,Calendar和Date patten -
 * 日期格式可以不提供默认就是yyyyMMdd HH:mm:ss
 * 
 * 
 * @author wei.zhang
 * 
 */
public class ToChar extends BaseSingleRowFunction {
	/**
	 * 函数参数
	 */
	private PrefixAndSuffix valuePas;
	/**
	 * 函数参数
	 */
	private SimpleDateFormat patten;

	private int qindex = -1;

	private int qqindex = -1;

	private int windex = -1;

	private int mmwindex = -1;

	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Object obj = valuePas.getValue(row);
		if (obj == null) {
			return null;
		}
		String result = null;
		if (obj instanceof Long) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis((Long) obj);
			result = patten.format(cal.getTime());
			if (cal.get(Calendar.MONTH) == Calendar.DECEMBER) {
				if (windex > 0 && result.substring(windex).equals("1")) {
					result = (Integer.parseInt(result.substring(0, 4)) + 1) + "1";
				} else if (mmwindex > 0 && result.substring(mmwindex).equals("1")) {
					result = (Integer.parseInt(result.substring(0, 4)) + 1) + "011";
				}
			}
		} else if (obj instanceof Calendar) {
			result = patten.format(((Calendar) obj).getTime());
			Calendar cal = (Calendar) obj;
			if (cal.get(Calendar.MONTH) == Calendar.DECEMBER) {
				if (windex > 0 && result.substring(windex).equals("1")) {
					result = (Integer.parseInt(result.substring(0, 4)) + 1) + "1";
				} else if (mmwindex > 0 && result.substring(mmwindex).equals("1")) {
					result = (Integer.parseInt(result.substring(0, 4)) + 1) + "011";
				}
			}
		} else if (obj instanceof Date) {
			result = patten.format((Date) obj);
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) obj);
			if (cal.get(Calendar.MONTH) == Calendar.DECEMBER) {
				if (windex > 0 && result.substring(windex).equals("1")) {
					result = (Integer.parseInt(result.substring(0, 4)) + 1) + "1";
				} else if (mmwindex > 0 && result.substring(mmwindex).equals("1")) {
					result = (Integer.parseInt(result.substring(0, 4)) + 1) + "011";
				}
			}
		} else {
			throw new CalculationException("不支持的日期格式类型");
		}

		if (qindex != -1) {
			String q = result.substring(qindex, qindex + 2);
			if (q.equals("01") || q.equals("02") || q.equals("03")) {
				q = "1";
			} else if (q.equals("04") || q.equals("05") || q.equals("06")) {
				q = "2";
			} else if (q.equals("07") || q.equals("08") || q.equals("09")) {
				q = "3";
			} else if (q.equals("10") || q.equals("11") || q.equals("12")) {
				q = "4";
			} else {
				throw new CalculationException("季度格式格式化错误");
			}
			result = result.substring(0, qindex) + q + result.substring(qindex + 2);
		} else if (qqindex != -1) {
			String q = result.substring(qqindex, qqindex + 2);
			if (q.equals("01") || q.equals("02") || q.equals("03") || q.equals("04") || q.equals("05") || q.equals("06")) {
				q = "1";
			} else if (q.equals("07") || q.equals("08") || q.equals("09") || q.equals("10") || q.equals("11") || q.equals("12")) {
				q = "2";
			} else {
				throw new CalculationException("半年格式格式化错误");
			}
			result = result.substring(0, qqindex) + q + result.substring(qqindex + 2);
		}
		return result;
	}

	public static String toYearAndWeek(Date date) {
		String p = "yyyyw";
		SimpleDateFormat patten = new SimpleDateFormat(p);
		int windex = p.indexOf("w");
		String result = patten.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER) {
			if (result.substring(windex).equals("1")) {
				result = (Integer.parseInt(result.substring(0, 4)) + 1) + "1";
			}
		}
		return result;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		if (params == null || !(params.size() == 2 || params.size() == 1)) {
			throw new DplParseException();
		}
		valuePas = this.pasList.get(0);
		if (params.size() == 2) {
			String p = this.pasList.get(1).getConstantsValue();
			if (p.contains("QQ")) {
				// 半年
				qqindex = p.indexOf("QQ");
				p = p.replace("QQ", "MM");
			} else if (p.contains("Q")) {
				// 季度
				qindex = p.indexOf("Q");
				p = p.replace("Q", "MM");
			} else if (p.equals("yyyyw")) {
				windex = p.indexOf("w");
			} else if (p.equals("yyyyMMw")) {
				mmwindex = p.indexOf("w");
			}
			patten = new SimpleDateFormat(p);
		} else {
			patten = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		}
	}

	@Override
	public boolean isSupportWhere() {
		return true;
	}

}

// $Id: ToChar.java 14104 2010-06-04 23:27:36Z wei.zhang $