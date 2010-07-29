/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.impl;

import com.clican.pluto.dataprocess.engine.DataProcessTransaction;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 用于支持在各个DataProcess节点上单独的启动一个新的Transaction
 * <p>
 * 比较常用的场景是在循环节点每次循环做一次提交操作
 * 
 * @author wei.zhang
 * 
 */
public class DataProcessTransactionImpl implements DataProcessTransaction {

	
	public DataProcessor doInCommit(DataProcessor processor, ProcessorContext context) throws DataProcessException {
		processor.beforeProcess(context);
		processor.process(context);
		return processor.afterProcess(context);
	}

	
	public void doOneProcessInCommit(DataProcessor processor, ProcessorContext context) throws DataProcessException {
		processor.process(context);
	}

}

// $Id: DataProcessTransactionImpl.java 15293 2010-06-24 08:06:38Z wei.zhang $