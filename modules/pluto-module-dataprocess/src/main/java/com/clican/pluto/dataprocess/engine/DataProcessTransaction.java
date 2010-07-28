/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine;

import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 把一个单独的DataProcess节点包装在一个REQUIRED_NEW的Transaction中执行
 * 
 * @author wei.zhang
 * 
 */
public interface DataProcessTransaction {

	/**
	 * 在一个完整的Transaction中执行数据库操作
	 * 
	 * @param processor
	 *            需要执行的<code>DataProcessor</code>
	 * 
	 * @param context
	 *            <code>ProcessorContext</code>执行时的上下文
	 * @throws DataProcessException
	 */
	public DataProcessor doInCommit(DataProcessor processor, ProcessorContext context) throws DataProcessException;

	/**
	 * 对单步的Processor执行一个Transaction
	 * 
	 * @param processor
	 *            需要执行的<code>DataProcessor</code>
	 * @param context
	 *            <code>ProcessorContext</code>执行时的上下文
	 * @throws DataProcessException
	 */
	public void doOneProcessInCommit(DataProcessor processor, ProcessorContext context) throws DataProcessException;

}

// $Id: DataProcessTransaction.java 16015 2010-07-12 08:54:50Z wei.zhang $