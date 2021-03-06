/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.exception.DplParseException;

public abstract class BaseFunction implements Function {

	protected final Log log = LogFactory.getLog(getClass());

	protected final Log tracesLog = LogFactory.getLog("com.jsw.dataprocess.engine.processes.DplExecProcessor.tracesLog");

	protected List<Object> params;

	protected List<PrefixAndSuffix> pasList;

	protected String id;

	protected String expr;

	protected String trace;

	/**
	 * 最后计算得到的结果数值在select中的名称
	 */
	protected String columnName;

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public BaseFunction() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public void setParams(List<Object> params) throws DplParseException {
		this.params = params;
		this.pasList = getPrefixeAndSuffix(params);
	}

	public List<Object> getParams() {
		return this.params;
	}

	public List<String> getFromParams() {
		Set<String> fromParams = new HashSet<String>();
		for (PrefixAndSuffix pas : this.pasList) {
			fromParams.addAll(pas.getFromParams());
		}
		return new ArrayList<String>(fromParams);
	}

	private List<PrefixAndSuffix> getPrefixeAndSuffix(List<Object> params) throws DplParseException {
		List<PrefixAndSuffix> list = new ArrayList<PrefixAndSuffix>();
		for (Object param : params) {
			PrefixAndSuffix pas;
			if (param instanceof Function) {
				pas = new PrefixAndSuffix((Function) param);
			} else if (param instanceof String) {
				pas = new PrefixAndSuffix(param.toString());
			} else if (param instanceof PrefixAndSuffix) {
				pas = (PrefixAndSuffix) param;
			} else {
				// 这种情况绝对不可能发生,如果产生这个情况请检查FunctionParser的代码是否有问题
				throw new DplParseException("函数参数解析错误");
			}
			list.add(pas);
		}
		return list;
	}

	public boolean isLazyCalc() {
		return true;
	}

}

// $Id: BaseFunction.java 13254 2010-05-26 09:50:41Z wei.zhang $