/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.engine.DataProcessTransaction;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.enumeration.TransactionMode;
import com.clican.pluto.dataprocess.exception.DataProcessException;
import com.clican.pluto.dataprocess.exception.InterruptedException;

/**
 * 数据处理的一个通用基类实现，提供了默认的beforeProcess和afterProcess实现
 * 
 * @author wei.zhang
 * 
 */
public abstract class BaseDataProcessor implements DataProcessor {

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * 如果某个DataProcessor需要在单个Transaction节点执行那就需要用
	 * <code>DataProcessTransaction</code>
	 * <p>
	 * Spring的配置可以保证DataProcessTransaction的方法调用使用一个新的Transaction
	 */
	protected DataProcessTransaction dataProcessTransaction;

	/**
	 * <p>
	 * 设置Transaction的模式
	 * </p>
	 * <p>
	 * 如果不设置的话就是默认不做特殊的transaction
	 * </p>
	 * 
	 * @see TransactionMode
	 */
	private String transaction;

	private boolean destroy;

	/**
	 * 是否是开始节点
	 */
	private boolean startProcessor;

	/**
	 * Spring中的id
	 */
	private String id;

	/**
	 * 是否把Context克隆下传递给下一个循环节点
	 */
	protected boolean cloneContext;

	/**
	 * 如果cloneContext是true的话,那么该属性就标志了需要把下面循环节点产生的变量从子context中再返回会主context
	 */
	protected List<String> propagations;

	/**
	 * 下面的节点列表
	 */
	protected List<DataProcessor> nextDataProcessors;

	public List<DataProcessor> getNextDataProcessors() {
		return nextDataProcessors;
	}

	public void setNextDataProcessors(List<DataProcessor> nextDataProcessors) {
		this.nextDataProcessors = nextDataProcessors;
	}

	public boolean isStartProcessor() {
		return startProcessor;
	}

	public void setStartProcessor(boolean startProcessor) {
		this.startProcessor = startProcessor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public void setDataProcessTransaction(DataProcessTransaction dataProcessTransaction) {
		this.dataProcessTransaction = dataProcessTransaction;
	}

	public boolean isCloneContext() {
		return cloneContext;
	}

	public void setCloneContext(boolean cloneContext) {
		this.cloneContext = cloneContext;
	}

	public List<String> getPropagations() {
		return propagations;
	}

	public void setPropagations(List<String> propagations) {
		this.propagations = propagations;
	}

	/**
	 * 该方法当Spring Context Destroy的时候会被调用
	 */
	public void destroy() {
		this.destroy = true;
	}

	/**
	 * 调用下一个DataProcessor去处理数据，子类可以根据需要覆盖该实现。
	 */
	@Override
	public DataProcessor afterProcess(ProcessorContext context) throws DataProcessException {
		if (log.isTraceEnabled()) {
			log.trace("after [" + id + "] processes data");
		}
		if (destroy) {
			// 当Spring Context Destroy的时候就强行中断当前的Data Process
			throw new InterruptedException("流程在节点[" + id + "]被强行终止");
		}
		DataProcessor nextOne = null;
		try {
			if (nextDataProcessors != null && nextDataProcessors.size() > 0) {
				if (nextDataProcessors.size() == 1) {
					nextOne = nextDataProcessors.get(0);
					String nextOneTransaction = nextOne.getTransaction();
					// 如果下个处理节点有Transaction
					if (StringUtils.isNotEmpty(nextOneTransaction)) {
						TransactionMode mode = TransactionMode.convert(nextOneTransaction);
						if (mode == TransactionMode.COMMIT) {
							// 如果TransactionMode是COMMIT则对下个节点执行一个REQUIRED_NEW
							// Transaction
							nextOne.beforeProcess(context);
							this.dataProcessTransaction.doOneProcessInCommit(nextOne, context);
							return nextOne.afterProcess(context);
						} else if (mode == TransactionMode.BEGIN) {
							// 如果TransactionMode是BEGIN则对下个节点开始一个新的Transaction
							DataProcessor dp = this.dataProcessTransaction.doInCommit(nextOne, context);
							if (dp != null) {
								return dp.afterProcess(context);
							}
						} else if (mode == TransactionMode.END) {
							// 如果TransactionMode是END则对下个节点结束当前存在的Transaction
							nextOne.beforeProcess(context);
							nextOne.process(context);
							return nextOne;
						} else {
							throw new DataProcessException("该TransactionMode未定义[" + nextOneTransaction + "]");
						}
					} else {
						// 没有Transaction的情况下则直接沿用目前的Transaction情况。
						nextOne.beforeProcess(context);
						nextOne.process(context);
						return nextOne.afterProcess(context);
					}
				} else {
					for (int i = 0; i < nextDataProcessors.size(); i++) {
						DataProcessor nextDataProcessor = nextDataProcessors.get(i);
						ProcessorContext cloneOne = context.getCloneContext();
						nextOne = nextDataProcessor;
						String nextOneTransaction = nextOne.getTransaction();
						// 其主要的TransactionMode和只有单个next节点的情况基本一致
						if (StringUtils.isNotEmpty(nextOneTransaction)) {
							TransactionMode mode = TransactionMode.convert(nextOneTransaction);
							if (mode == TransactionMode.COMMIT) {
								nextOne.beforeProcess(cloneOne);
								this.dataProcessTransaction.doOneProcessInCommit(nextOne, cloneOne);
								return nextOne.afterProcess(cloneOne);
							} else if (mode == TransactionMode.BEGIN) {
								DataProcessor dp = this.dataProcessTransaction.doInCommit(nextOne, cloneOne);
								if (dp != null) {
									return dp.afterProcess(cloneOne);
								}
							} else if (mode == TransactionMode.END) {
								nextOne.beforeProcess(cloneOne);
								nextOne.process(cloneOne);
								// 在多个节点并行处理的情况下只有最后一个并行节点能够结束当前的Transaction
								if (i == nextDataProcessors.size() - 1) {
									return nextOne;
								} else {
									// 否则就抛出错误
									throw new DataProcessException("对于同时处理多个nextDataProcessors的情况下,只有最后一个的nextDataProcessor可以设置end transaction");
								}
							} else {
								throw new DataProcessException("该TransactionMode未定义[" + nextOneTransaction + "]");
							}
						} else {
							nextOne.beforeProcess(cloneOne);
							nextOne.process(cloneOne);
							if (i == nextDataProcessors.size() - 1) {
								return nextOne.afterProcess(cloneOne);
							} else {
								nextOne.afterProcess(cloneOne);
							}
						}
					}
				}
			}
		} catch (DataProcessException e) {
			throw e;
		} catch (Exception e) {
			if (nextOne instanceof BaseDataProcessor) {
				throw new DataProcessException("调用[" + ((BaseDataProcessor) nextOne).getId() + "]出错", e);
			} else {
				throw new DataProcessException("调用[" + nextOne + "]出错", e);
			}
		}
		return null;
	}

	/**
	 * 简单输出该DataProcessor被调用的日志，子类可以根据需要覆盖该实现。
	 */
	@Override
	public void beforeProcess(ProcessorContext context) throws DataProcessException {
		if (log.isTraceEnabled()) {
			log.trace("before [" + id + "] processes data");
		}
	}

}

// $Id: BaseDataProcessor.java 16019 2010-07-12 09:12:33Z wei.zhang $