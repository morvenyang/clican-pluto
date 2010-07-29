/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.ha.framework.interfaces.ClusterNode;
import org.jboss.ha.framework.interfaces.HAPartition;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 进行分布式计算的处理节点
 * 
 * @author wei.zhang
 * 
 */
public class JGroupPartitionProcessor extends BaseDataProcessor {

	private List<DataProcessor> partitionProcessors;

	/**
	 * 通过JNDI获得的HAPartition
	 */
	private HAPartition partition;

	/**
	 * 需要分布计算的list的名称
	 */
	private String partitionListName;

	/**
	 * 其他要作为输入的变量名
	 */
	private String[] inputVarName;

	/**
	 * 输出的变量名
	 */
	private String[] outputVarName;

	/**
	 * 在HAPartition中的service name该name必须唯一
	 */
	private String serviceName;

	private Thread currentThread;

	public void setPartitionProcessors(List<DataProcessor> partitionProcessors) {
		this.partitionProcessors = partitionProcessors;
	}

	public void setPartition(HAPartition partition) {
		this.partition = partition;
	}

	public void setPartitionListName(String partitionListName) {
		this.partitionListName = partitionListName;
	}

	public void setInputVarName(String[] inputVarName) {
		this.inputVarName = inputVarName;
	}

	public void setOutputVarName(String[] outputVarName) {
		this.outputVarName = outputVarName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void init() {
		if (partition != null) {
			partition.registerRPCHandler(serviceName, this);
		}
	}

	public void destroy() {
		super.destroy();
		if (partition != null) {
			try {
				partition.unregisterRPCHandler(serviceName, this);
			} catch (Throwable e) {
				log.error("", e);
			}
		}
		if (currentThread != null) {
			try {
				synchronized (currentThread) {
					currentThread.notify();
				}
			} catch (Throwable e) {
				log.error("", e);
			}
		}
	}

	public ProcessorContext partitionProcess(ProcessorContext subContext) throws DataProcessException {
		if (log.isDebugEnabled()) {
			log.debug("开始分布式调用计算[" + getId() + "]");
		}
		for (DataProcessor partitionProcessor : partitionProcessors) {
			partitionProcessor.beforeProcess(subContext);
			partitionProcessor.process(subContext);
			partitionProcessor.afterProcess(subContext);
		}

		if (log.isDebugEnabled()) {
			log.debug("结束分布式调用计算[" + getId() + "]");
		}
		return subContext;
	}

	@SuppressWarnings("unchecked")
	
	public synchronized void process(final ProcessorContext context) throws DataProcessException {
		if (partition == null) {
			partitionProcess(context);
		} else {
			currentThread = Thread.currentThread();
			// 用来统计返回结果数
			final AtomicInteger returnCount = new AtomicInteger(0);
			// 用来统计异步调用的错误
			final List<Throwable> exceptionList = new ArrayList<Throwable>();
			// 获得要分布式计算的对象
			List<Object> partitionList = context.getAttribute(partitionListName);
			ClusterNode[] clusterNodes = partition.getClusterNodes();
			final List<ProcessorContext> returnContextList = new ArrayList<ProcessorContext>();
			final int nodes = clusterNodes.length;
			int length = partitionList.size() / nodes;
			for (int i = 0; i < nodes; i++) {
				final ClusterNode clusterNode = clusterNodes[i];
				ProcessorContext subContext = new ProcessorContextImpl();
				if (inputVarName != null) {
					for (String name : inputVarName) {
						name = name.trim();
						Object value = context.getAttribute(name);
						subContext.setAttribute(name, value);
					}
				}
				if (partitionList.size() < (i + 1) * length) {
					subContext.setAttribute(partitionListName, new ArrayList<Object>(partitionList.subList(i * length, partitionList.size())));
				} else {
					subContext.setAttribute(partitionListName, new ArrayList<Object>(partitionList.subList(i * length, (i + 1) * length)));
				}
				Thread t = new Thread() {
					
					public void run() {
						try {
							ProcessorContext returnContext = (ProcessorContext) partition.callMethodOnNode(serviceName, "partitionProcess",
									new Object[] { context }, new Class[] { ProcessorContext.class }, -1, clusterNode);
							returnContextList.add(returnContext);
							returnCount.incrementAndGet();
							if (returnCount.intValue() >= nodes) {
								synchronized (currentThread) {
									currentThread.notify();
								}
							}
						} catch (Throwable e) {
							exceptionList.add(e);
							log.error("分布式计算错误", e);
							synchronized (currentThread) {
								currentThread.notify();
							}
						}
					}
				};
				t.start();
			}

			synchronized (currentThread) {
				try {
					currentThread.wait();
				} catch (Exception e) {
					throw new DataProcessException("Jgroup分布计算错误[" + this.getId() + "]", e);
				}
			}

			try {
				if (exceptionList.size() != 0) {
					throw exceptionList.get(0);
				}
			} catch (Throwable e) {
				throw new DataProcessException("分布式计算错误", e);
			}

			for (ProcessorContext returnContext : returnContextList) {
				for (String name : outputVarName) {
					name = name.trim();
					Object returnObject = returnContext.getAttribute(name);
					if (returnObject instanceof List) {
						if (context.getAttribute(name) != null) {
							((List) context.getAttribute(name)).addAll((List) returnObject);
						} else {
							context.setAttribute(name, returnObject);
						}
					} else {
						throw new DataProcessException("分布式计算的返回结果必须是一个List");
					}
				}
			}
		}
	}
}

// $Id$