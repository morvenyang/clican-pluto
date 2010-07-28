/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplException;

/**
 * Date Process Language的处理类，该类的实现接口可以用来根据dpl描述的查询语句
 * <code>ProcessorContext</code>的上下文中把希望获得的数据各类where条件、group by条件order
 * by条件再经过各个calculation计算获得结果。
 * 
 * @author wei.zhang
 * 
 */
public interface DplStatement {

	/**
	 * 
	 * @param <T>
	 * @param dpl
	 *            dpl data process language
	 * @param context
	 *            数据来源的上下文
	 * @param clazz
	 *            把查询得到的结果转换为该数据类型，要求必须查询结果的as名称和他的property
	 *            name完全一致。该clazz必须有一个没有参数的构造函数。
	 * @return
	 * @throws DplException
	 *             如果在解析dpl或执行dpl的过程中出现错误则抛出该异常
	 */
	public <T> List<T> execute(String dpl, ProcessorContext context, Class<T> clazz) throws DplException;

	/**
	 * 
	 * @param dpl
	 *            data process language
	 * @param context
	 *            数据来源的上下文
	 * @return
	 * @throws DplException
	 *             如果在解析dpl或执行dpl的过程中出现错误则抛出该异常
	 */
	public List<Map<String, Object>> execute(String dpl, ProcessorContext context) throws DplException;

	/**
	 * 
	 * @param <T>
	 * @param dpl
	 *            data process language
	 * @param context
	 *            数据来源的上下文
	 * @param clazz
	 *            把查询得到的结果转换为该数据类型，要求必须查询结果的as名称和他的property
	 *            name完全一致。该clazz必须有一个没有参数的构造函数。
	 * @return
	 * @throws DplException
	 */
	public <T> List<T> execute(String dpl, Map<String, Object> context, Class<T> clazz) throws DplException;

	/**
	 * 
	 * @param dpl
	 *            data process language
	 * @param context
	 *            数据来源的上下文
	 * @return
	 * @throws DplException
	 */
	public List<Map<String, Object>> execute(String dpl, Map<String, Object> context) throws DplException;

}

// $Id: DplStatement.java 13278 2010-05-26 11:29:38Z wei.zhang $