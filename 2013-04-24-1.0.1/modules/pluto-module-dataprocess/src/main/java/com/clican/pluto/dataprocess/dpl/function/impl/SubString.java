/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 截取字符串
 *
 * @author clican
 *
 */
public class SubString extends BaseSingleRowFunction {

	/**
	 * 被截取的字符串
	 */
	private PrefixAndSuffix str;

	/**
	 * 截取的起始位置
	 */
	private PrefixAndSuffix begin;

	/**
	 * 截取的结束位置,如果为空则截到末尾
	 */
	private PrefixAndSuffix end;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		String s = str.getValue(row);
		Number b = begin.getValue(row);
		if (end != null) {
			Number e = end.getValue(row);
			return s.substring(b.intValue(), e.intValue());
		} else {
			return s.substring(b.intValue());
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		str = this.pasList.get(0);
		begin = this.pasList.get(1);
		if (this.pasList.size() > 2) {
			end = this.pasList.get(2);
		}
	}

}

// $Id: SubString.java 16263 2010-07-16 09:24:00Z wei.zhang $