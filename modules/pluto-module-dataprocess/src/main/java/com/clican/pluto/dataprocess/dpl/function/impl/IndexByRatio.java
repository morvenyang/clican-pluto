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
 * 根据单个收益率计算指数,根据对应的是否结转来计算指数
 * <p>
 * indexByRatio(double ratio,boolean convert)
 * 
 * @author clican
 * 
 */
public class IndexByRatio extends BaseSingleRowFunction {

	public final static Double DEFAULT_INDEX = 10000d;

	private Double index = DEFAULT_INDEX;

	private PrefixAndSuffix ratio;

	private Double previousIndex;

	private Double previousConvertIndex;

	private PrefixAndSuffix convert;

	public Object calculate(Map<String, Object> row)
			throws CalculationException, PrefixAndSuffixException {
		if(index==null){
			index = this.pasList.get(2).getConstantsValue(Double.class);
		}
		boolean con = Boolean.parseBoolean(convert.getValue(row)
				.toString());
		Number ratioValue = ratio.getValue(row);
		if (ratioValue == null) {
			ratioValue = 0;
		}
		if (previousIndex == null || previousConvertIndex == null) {
			previousIndex = index;
			previousConvertIndex = index;
		}
		previousIndex = previousIndex + ratioValue.doubleValue()
				* previousConvertIndex;
		if (con) {
			previousConvertIndex = previousIndex;
		}
		return previousIndex;
	}

	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		this.ratio = this.pasList.get(0);
		this.convert = this.pasList.get(1);

	}

}

// $Id: IndexByRatio.java 14954 2010-06-17 08:43:16Z wei.zhang $