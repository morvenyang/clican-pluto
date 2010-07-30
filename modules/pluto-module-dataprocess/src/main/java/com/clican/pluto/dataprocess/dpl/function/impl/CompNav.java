/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算复合净值的函数
 * 
 * 该函数参数描述
 * 
 * compNav(double nav,double div,double split)
 * 
 * nav - 净值 div - 分红 split - 拆分
 * 
 * @author wei.zhang
 * 
 */
public class CompNav extends BaseSingleRowFunction {

	private PrefixAndSuffix navPas;

	private PrefixAndSuffix divPas;

	private PrefixAndSuffix splitPas;

	private Double previousNav;

	private Double previousCompNav;

	private PrefixAndSuffix prevNav;

	private PrefixAndSuffix prevCompNav;

	private Double previousSplit;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		if (previousNav == null) {
			previousNav = prevNav.getValue(row);
		}
		if (previousCompNav == null) {
			previousCompNav = prevCompNav.getValue(row);
		}
		Double nav = navPas.getValue(row);
		Double actualNav = nav;
		Double div = divPas.getValue(row);
		Double split = splitPas.getValue(row);
		if (nav == null || nav == 0) {
			throw new CalculationException("净值不能为空或0");
		}
		if (split != null && split != 0 && split != previousSplit) {
			nav = nav * split;
			previousSplit = split;
		}
		if (div != null) {
			nav = nav + div;
		}
		Double result;
		if (previousNav == null) {
			result = 0d;
		} else {
			result = nav / previousNav - 1;
		}

		previousNav = actualNav;

		if (previousCompNav == null) {
			previousCompNav = actualNav;
		} else {
			previousCompNav = (1 + result) * previousCompNav;
		}
		return previousCompNav;
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		navPas = this.pasList.get(0);
		divPas = this.pasList.get(1);
		splitPas = this.pasList.get(2);
		prevNav = this.pasList.get(3);
		prevCompNav = this.pasList.get(4);
	}
}

// $Id: CompNav.java 12555 2010-05-17 03:44:01Z wei.zhang $