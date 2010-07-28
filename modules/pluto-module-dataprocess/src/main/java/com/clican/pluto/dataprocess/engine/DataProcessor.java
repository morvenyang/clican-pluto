/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine;

import com.clican.pluto.dataprocess.enumeration.TransactionMode;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 数据处理的接口类，所有的数据处理的实现类都必须通过实现该接口来集成到本系统中。
 * 
 * @author wei.zhang
 * 
 */
public interface DataProcessor {

	/**
	 * 根据当前的<code>ProcessorContext</code>来处理数据
	 * 
	 * @param context
	 * @throws DataProcessException
	 *             由实现类自定义是否需要抛出数据处理的错误
	 */
	public void process(ProcessorContext context) throws DataProcessException;

	/**
	 * 在处理前被调用
	 * 
	 * @param context
	 */
	public void beforeProcess(ProcessorContext context) throws DataProcessException;

	/**
	 * 
	 * 在处理后被调用
	 * 
	 * @param context
	 * @return 如果该处理节点的Transaction需要结束的话则返回下一个处理节点给上层。 然后通过
	 *         <code>DataProcessTransaction</code>来包装下个节点的Transaction。
	 *         如果Transaction不需要结束则返回空
	 * @throws DataProcessException
	 */
	public DataProcessor afterProcess(ProcessorContext context) throws DataProcessException;

	/**
	 * 返回当前节点事物模式的表达式
	 * 
	 * @see TransactionMode
	 * @return
	 */
	public String getTransaction();

}

// $Id: DataProcessor.java 15292 2010-06-24 08:03:46Z wei.zhang $