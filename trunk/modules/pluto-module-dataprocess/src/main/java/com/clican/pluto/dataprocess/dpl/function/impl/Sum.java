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
 * 实现了多行数据的汇总，被汇总的字段必须是数字类型。
 * <p>
 * 如果被汇总的字段不是数字类型则会抛出<code>CalculationException</code>
 * 
 * @author wei.zhang
 * 
 */
public class Sum extends BaseMultiRowFunction {
	/**
	 * 函数参数
	 */
	private PrefixAndSuffix prefixAndSuffix;

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);
		prefixAndSuffix = this.pasList.get(0);
	}

	
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		Number result = null;
		for (Map<String, Object> row : rowSet) {
			Object obj = prefixAndSuffix.getValue(row);
			if (obj == null) {
				continue;
			} else {
				if (result == null) {
					if(obj instanceof Number){
						result = ((Number) obj).doubleValue();
					} else {
						throw new CalculationException("不支持的数字类型");
					}
				} else if (obj instanceof Number) {
					result = (Double) result + ((Number) obj).doubleValue();
				}else {
					throw new CalculationException("不支持的数字类型");
				}
			}
		}
		if (result == null) {
			return 0;
		} else {
			return result;
		}
	}

}

// $Id: Sum.java 15883 2010-07-08 06:32:18Z wei.zhang $