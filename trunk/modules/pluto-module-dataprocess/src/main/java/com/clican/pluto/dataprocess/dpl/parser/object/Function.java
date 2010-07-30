/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object;

import java.util.List;

import com.clican.pluto.dataprocess.dpl.parser.ParserObject;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 函数接口
 * 
 * @author clican
 * 
 */
public interface Function extends ParserObject {

	/**
	 * 返回函数参数
	 * 
	 * @return
	 */
	public List<Object> getParams();

	/**
	 * 设置函数参数
	 * 
	 * @param params
	 * @param context
	 * @throws DplParseException
	 */
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException;

	/**
	 * 返回在From关键字中出现过的函数参数
	 * 
	 * @return
	 */
	public List<String> getFromParams();

	/**
	 * 返回计算结果的名称
	 * 
	 * @return
	 */
	public String getColumnName();

	/**
	 * 设置计算结果的名称
	 * 
	 * @param columnName
	 */
	public void setColumnName(String columnName);

	/**
	 * 是否支持在where语句中使用
	 * 
	 * @return
	 */
	public boolean isSupportWhere() throws DplParseException;

	/**
	 * 返回函数的唯一标识id，有点类似内存id
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 返回函数表达式
	 * 
	 * @return
	 */
	public String getExpr();

	/**
	 * 设置函数表达式
	 * 
	 * @param expr
	 */
	public void setExpr(String expr);

	/**
	 * 返回函数返回值在trace中显示的名称
	 * 
	 * @return
	 */
	public String getTrace();

	/**
	 * 设置函数返回值在trace中显示的名称
	 * 
	 * @param trace
	 */
	public void setTrace(String trace);

	/**
	 * 是否需要延迟计算
	 * 
	 * @return
	 */
	public boolean isLazyCalc();

}

// $Id: Function.java 16004 2010-07-12 06:43:40Z wei.zhang $