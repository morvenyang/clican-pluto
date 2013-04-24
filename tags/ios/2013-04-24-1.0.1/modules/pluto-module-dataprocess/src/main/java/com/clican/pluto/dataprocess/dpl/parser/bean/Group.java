/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

import java.util.Map;

import com.clican.pluto.dataprocess.dpl.function.MultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.impl.FromParserImpl;
import com.clican.pluto.dataprocess.exception.DplException;
import com.clican.pluto.dataprocess.exception.DplParseException;

public class Group {

	private String expr;
	
	private PrefixAndSuffix prefixAndSuffix;

	public PrefixAndSuffix getPrefixAndSuffix() {
		return prefixAndSuffix;
	}

	public void setPrefixAndSuffix(PrefixAndSuffix prefixAndSuffix) {
		this.prefixAndSuffix = prefixAndSuffix;
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
			if (prefixAndSuffix.getFunction() instanceof SingleRowFunction) {
				return ((SingleRowFunction) prefixAndSuffix.getFunction()).recurseCalculate(null, row);
			} else if (prefixAndSuffix.getFunction() instanceof MultiRowFunction) {
				throw new DplException("The function in group by can't be MultiRowFunction");
			} else {
				if (prefixAndSuffix.getPrefix().equals(FromParserImpl.CONSTANTS_KEY)) {
					throw new DplParseException("无法使用常量[" + prefixAndSuffix.getExpr() + "]作为GroupBy的对象");
				}
				return prefixAndSuffix.getValue(row);
			}
		} catch (DplException e) {
			throw e;
		} catch (Exception e) {
			throw new DplParseException(e);
		}

	}

}

// $Id: Group.java 16227 2010-07-16 02:56:24Z wei.zhang $