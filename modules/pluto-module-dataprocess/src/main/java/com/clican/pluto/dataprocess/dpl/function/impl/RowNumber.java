/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.function.MultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 计算当前记录在整个list中处于第几行,从0开始计
 * 
 * @author wei.zhang
 * 
 */
public class RowNumber extends BaseFunction implements SingleRowFunction {

	/**
	 * list数据集合
	 */
	private PrefixAndSuffix listValue;

	/**
	 * 当前记录行
	 */
	private PrefixAndSuffix value;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		throw new CalculationException("This method shall never be invoked");
	}

	
	public boolean containMultiRowCalculation() {
		for (PrefixAndSuffix pas : this.pasList) {
			if (pas.getFunction() instanceof MultiRowFunction) {
				return true;
			}
		}
		return false;
	}

	
	public Object recurseCalculate(List<Map<String, Object>> rowSet, Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		List<Object> list = listValue.getValue(row);
		Object v = value.getValue(row);
		return new Integer(list.indexOf(v)).doubleValue();
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		this.listValue = this.pasList.get(0);
		this.value = this.pasList.get(1);
	}
}

// $Id: RowNumber.java 16258 2010-07-16 09:18:29Z wei.zhang $