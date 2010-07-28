/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.enumeration.TransactionMode;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 判断分支的Processor节点
 * 
 * @author wei.zhang
 * 
 */
public class ConditionProcessor extends BaseDataProcessor {

	private Map<String, DataProcessor> dataProcessorMap;

	private Map<String, String> exceptionMap;

	public void setDataProcessorMap(Map<String, DataProcessor> dataProcessorMap) {
		this.dataProcessorMap = dataProcessorMap;
	}

	public void setExceptionMap(Map<String, String> exceptionMap) {
		this.exceptionMap = exceptionMap;
	}

	@Override
	public void process(ProcessorContext context) throws DataProcessException {
		// do nothing
	}

	@Override
	public DataProcessor afterProcess(ProcessorContext context) throws DataProcessException {
		if (log.isTraceEnabled()) {
			log.trace("after" + getClass().getSimpleName() + " processes data");
		}
		try {
			if (exceptionMap == null || exceptionMap.size() > 0) {
				for (String mvel : exceptionMap.keySet()) {
					if (MVEL.evalToBoolean(mvel, context.getMap())) {
						String exception = exceptionMap.get(mvel);
						throw new DataProcessException(exception);
					}
				}
			}
			if (dataProcessorMap == null || dataProcessorMap.size() > 0) {
				for (String mvel : dataProcessorMap.keySet()) {
					if (MVEL.evalToBoolean(mvel, context.getMap())) {
						DataProcessor nextOne = dataProcessorMap.get(mvel);
						if (nextOne != null) {
							String nextOneTransaction = nextOne.getTransaction();
							if (StringUtils.isNotEmpty(nextOneTransaction)) {
								TransactionMode mode = TransactionMode.convert(nextOneTransaction);
								if (mode == TransactionMode.COMMIT) {
									nextOne.beforeProcess(context);
									this.dataProcessTransaction.doOneProcessInCommit(nextOne, context);
									nextOne.afterProcess(context);
								} else if (mode == TransactionMode.BEGIN) {
									DataProcessor dp = this.dataProcessTransaction.doInCommit(nextOne, context);
									if (dp != null) {
										dp.afterProcess(context);
									}
								} else if (mode == TransactionMode.END) {
									// 如果下个节点需要结束transaction
									nextOne.beforeProcess(context);
									nextOne.process(context);
									return null;
								} else {
									throw new DataProcessException("该TransactionMode未定义[" + nextOneTransaction + "]");
								}
							} else {
								nextOne.beforeProcess(context);
								nextOne.process(context);
								nextOne.afterProcess(context);
							}
						}
						break;
					}
				}
			}
		} catch (DataProcessException e) {
			throw e;
		}
		return null;
	}

}

// $Id: ConditionProcessor.java 15178 2010-06-23 04:44:41Z wei.zhang $