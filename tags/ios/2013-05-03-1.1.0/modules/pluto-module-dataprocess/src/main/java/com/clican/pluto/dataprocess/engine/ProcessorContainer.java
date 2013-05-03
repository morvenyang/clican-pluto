/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine;

import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 用来装载所有<code>DataProcessor</code>的容器
 * 
 * @author clican
 * 
 */
public interface ProcessorContainer {

	/**
	 * 调用某组Processor来处理数据
	 * 
	 * @param processorGroupName
	 * @throws DataProcessException
	 */
	public void processData(String processorGroupName) throws DataProcessException;

	/**
	 * 调用某组Processor来处理数据
	 * 
	 * @param processorGroupName
	 *            在Spring中定义的processorGroupName
	 * @param context
	 *            外部系统可以传递一个自定义的<code>ProcessorContext</code>,如果为空则内部系统使用默认的
	 *            <code>ProcessorContextImpl</code>
	 * @throws DataProcessException
	 */
	public void processData(String processorGroupName, ProcessorContext context) throws DataProcessException;

}

// $Id: ProcessorContainer.java 15178 2010-06-23 04:44:41Z wei.zhang $