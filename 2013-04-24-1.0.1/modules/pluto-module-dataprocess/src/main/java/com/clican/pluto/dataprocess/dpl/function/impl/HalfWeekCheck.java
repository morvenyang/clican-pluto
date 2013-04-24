/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 检查半周时间,根据date所在的周找出checkSet所包含的周，
 * 然后判断这些周如果有几天在n月有几天是n-1月
 * 则该天所在的周被划定到天数比较多的那个月份，
 * 如果天数一样则划分到n月
 * 
 * <p>
 * halfWeekCheck(Date date,Set<Date> checkSet)
 *
 * @author clican
 *
 */
public class HalfWeekCheck extends BaseSingleRowFunction {

	private Set<Date> checkSet;

	private PrefixAndSuffix value;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Date d = value.getValue(row);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar c3 = Calendar.getInstance();
		c1.setTime(d);
		c2.setTime(d);
		c3.setTime(d);
		int index = c1.get(Calendar.WEEK_OF_MONTH);
		if (index == 1) {
			int before = 0;
			int after = 1;
			c2.add(Calendar.DAY_OF_MONTH, -1);
			for (; c2.get(Calendar.WEEK_OF_YEAR) == c1.get(Calendar.WEEK_OF_YEAR); c2.add(Calendar.DAY_OF_MONTH, -1)) {
				if (checkSet.contains(c2.getTime())) {
					if (c2.get(Calendar.MONTH) == c1.get(Calendar.MONTH)) {
						after++;
					} else {
						before++;
					}
				}
			}
			c3.add(Calendar.DAY_OF_MONTH, 1);
			for (; c3.get(Calendar.WEEK_OF_YEAR) == c1.get(Calendar.WEEK_OF_YEAR); c3.add(Calendar.DAY_OF_MONTH, 1)) {
				if (checkSet.contains(c3.getTime())) {
					after++;
				}
			}
			if (before > after) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	
	@SuppressWarnings("unchecked")
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		value = this.pasList.get(0);
		checkSet = new HashSet<Date>((Collection<Date>) this.pasList.get(1).getConstantsValue());
	}

}

// $Id$