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
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class FundDr extends BaseSingleRowFunction {

	private PrefixAndSuffix navPas;

	private PrefixAndSuffix divPas;

	private PrefixAndSuffix splitPas;
	
	private PrefixAndSuffix prevNav;

	private Double previousNav;
	
	private Double previousSplit;

	@Override
	public Object calculate(Map<String, Object> row) throws CalculationException,PrefixAndSuffixException {
		if (previousNav == null) {
			previousNav = prevNav.getValue(row);
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
		if (div != null ) {
			nav = nav + div;
		}
		Double result;
		if (previousNav == null) {
			result = 0d;
		} else {
			result = nav / previousNav - 1;
		}
		
		previousNav = actualNav;
		return result;
	}

	@Override
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		navPas = this.pasList.get(0);
		divPas = this.pasList.get(1);
		splitPas = this.pasList.get(2);
		prevNav = this.pasList.get(3);
	}
}


//$Id: FundDr.java 15243 2010-06-23 13:25:07Z wei.zhang $