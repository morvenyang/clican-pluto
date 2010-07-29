/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算重仓风险度
 * <p>
 * ecr(List<Double> weights)
 *
 * @author wei.zhang
 *
 */
public class Ecr extends BaseMultiRowFunction {

	private PrefixAndSuffix pcTofNav;

	private int number = 10;

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		List<Map<String, Object>> rowSetCopy = new ArrayList<Map<String, Object>>(rowSet);
		Collections.sort(rowSetCopy, new Comparator<Map<String, Object>>() {
			
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				try {
					return -((Double) pcTofNav.getValue(o1)).compareTo((Double) pcTofNav.getValue(o2));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		Double sum = 0d;
		Double t = 0d;
		for (int i = 0; i < number; i++) {
			if(rowSetCopy.size()>i){
				t += (Double) pcTofNav.getValue(rowSetCopy.get(i));
			}
			sum += t * (i + 1);
		}
		return sum;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.pcTofNav = this.pasList.get(0);
		if (this.pasList.size() > 1) {
			this.number = Integer.parseInt((String)this.pasList.get(1).getConstantsValue());
		}
	}
}

// $Id: Ecr.java 13264 2010-05-26 10:08:11Z wei.zhang $