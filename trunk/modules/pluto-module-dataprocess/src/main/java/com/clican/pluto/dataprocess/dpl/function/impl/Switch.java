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
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 根据swtich方式来返回结果集合
 * 
 * switch(Object comparedObj,Object compareObj1,Object result1,Object compareObj2,Object result2,...,Object compareObjn,Object resultn)
 *
 * @author clican
 *
 */
public class Switch extends BaseSingleRowFunction {

	private PrefixAndSuffix condition;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Object cond = condition.getValue(row);
		for (int i = 1; i < this.pasList.size() && i != pasList.size() - 1; i = i + 2) {
			Object compare = pasList.get(i).getValue(row);
			if (cond == null && compare == null) {
				return pasList.get(i + 1).getValue(row);
			} else {
				if (cond instanceof String && compare instanceof String) {
					if (((String) cond).matches((String) compare) || ((String) cond).equals((String) compare)) {
						return pasList.get(i + 1).getValue(row);
					} else {
						continue;
					}
				} else {
					if (cond != null) {
						if (cond.equals(compare)) {
							return pasList.get(i + 1).getValue(row);
						} else {
							continue;
						}
					} else {
						continue;
					}
				}
			}
		}
		if (pasList.size() % 2 == 0) {
			// 有默认数值
			return pasList.get(pasList.size() - 1).getValue(row);
		} else {
			return null;
		}
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return true;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		condition = this.pasList.get(0);
	}

}

// $Id: Switch.java 16264 2010-07-16 09:25:42Z wei.zhang $