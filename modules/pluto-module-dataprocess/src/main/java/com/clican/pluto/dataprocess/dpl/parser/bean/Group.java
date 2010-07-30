/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

import java.util.Map;

import com.clican.pluto.common.util.PropertyUtilS;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.exception.DplException;
import com.clican.pluto.dataprocess.exception.DplParseException;

public class Group {

	/**
	 * 如果Group的内容是一个function则该属性不为空
	 */
	private SingleRowFunction function;

	/**
	 * 如果Group的内容是一个普通字段则该属性不为空
	 */
	private String expr;

	public void setFunction(SingleRowFunction function) {
		this.function = function;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	/**
	 * 由于把每行row传递给该方法得到GroupBy的Value
	 * 
	 * @param row
	 * @return
	 * @throws DplException
	 */
	public Object getValue(Map<String, Object> row) throws DplException {
		if (row == null) {
			return null;
		}
		try {
			if (function != null) {
				return function.recurseCalculate(null, row);
			} else {
				if (expr.startsWith("dual.")) {
					throw new DplParseException("无法使用常量[" + expr + "]作为GroupBy的对象");
				}
				return PropertyUtilS.getNestedProperty(row, expr);
			}
		} catch (DplException e) {
			throw e;
		} catch (Exception e) {
			throw new DplParseException(e);
		}

	}

}

// $Id: Group.java 16227 2010-07-16 02:56:24Z wei.zhang $