/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;


/**
 * 计算复合净值的函数
 * 
 * 该函数参数描述
 * 
 * multiCompNav(double nav,double div,double split)
 * 
 * nav - 净值
 * div - 分红
 * split - 拆分
 * 
 * @author wei.zhang
 *
 */
public class MultiCompNav extends BaseMultiRowFunction {

	private PrefixAndSuffix navPas;

	private PrefixAndSuffix divPas;

	private PrefixAndSuffix splitPas;

	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		List<Object> result = new ArrayList<Object>();
		Double previousNav = null;
		for (Map<String, Object> row : rowSet) {
			Double nav = navPas.getValue(row);
			Double actualNav = nav;
			Double div = divPas.getValue(row);
			Double split = splitPas.getValue(row);
			if (nav == null || nav == 0) {
				throw new CalculationException("净值不能为空或0");
			}
			if (split != null && split != 0) {
				nav = nav * split;
			}
			if (div != null) {
				nav = nav + div;
			}
			Double res;
			if (previousNav == null) {
				res = 0d;
			} else {
				res = nav / previousNav - 1;
			}
			previousNav = actualNav;
			result.add(res);
		}
		return result;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		if (params == null || !(params.size() == 2 || params.size() == 3)) {
			throw new DplParseException();
		}
		this.navPas = this.getPrefixeAndSuffix(params).get(0);
		this.divPas = this.getPrefixeAndSuffix(params).get(1);
		this.splitPas = this.getPrefixeAndSuffix(params).get(2);
	}

}

// $Id: MultiCompNav.java 12410 2010-05-13 06:55:57Z wei.zhang $