/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jboss.ha.framework.interfaces.ClusterNode;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.engine.processes.JGroupPartitionProcessor;
import com.clican.pluto.dataprocess.testbean.BeanA;

public class JGroupPartitionProcessorTestCase extends TestCase {

	private Mockery context = new Mockery();

	public void test1() throws Exception {
		JGroupPartitionProcessor p = new JGroupPartitionProcessor();
		final HAPartition partition = context.mock(HAPartition.class);
		p.setPartition(partition);
		final DataProcessor p1 = context.mock(DataProcessor.class);
		final ClusterNode n1 = context.mock(ClusterNode.class,"n1");
		final ClusterNode n2 = context.mock(ClusterNode.class,"n2");
		final ClusterNode[] cns = new ClusterNode[]{n1,n2};
		List<DataProcessor> partitionProcessors = new ArrayList<DataProcessor>();
		partitionProcessors.add(p1);
		p.setPartitionProcessors(partitionProcessors);
		p.setServiceName("test");
		p.setInputVarName(new String[] { "aaa" });
		p.setPartitionListName("list");
		p.setOutputVarName(new String[] { "bbb" });
		final ProcessorContext ctx = this.getContext();
		context.checking(new Expectations() {
			{
				one(partition).getClusterNodes();
				will(new CustomAction(""){
					
					public Object invoke(Invocation invocation) throws Throwable {
						return cns;
					}
					
				});
				try {
					one(partition).callMethodOnNode("test", "partitionProcess", (Object[]) with(anything()), new Class[] { ProcessorContext.class }, -1,n1);
				} catch (Throwable e) {

				}
				will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						ProcessorContext returnContext = new ProcessorContextImpl();
						List<Integer> result = new ArrayList<Integer>();
						result.add(1);
						returnContext.setAttribute("bbb", result);
						return returnContext;
					}
				});
				
				try {
					one(partition).callMethodOnNode("test", "partitionProcess", (Object[]) with(anything()), new Class[] { ProcessorContext.class }, -1,n2);
				} catch (Throwable e) {

				}
				will(new CustomAction("") {
					public Object invoke(Invocation invocation) throws Throwable {
						ProcessorContext returnContext = new ProcessorContextImpl();
						List<Integer> result = new ArrayList<Integer>();
						result.add(2);
						returnContext.setAttribute("bbb", result);
						return returnContext;
					}
				});

			}
		});
		p.process(ctx);
	}
	
	public void test2() throws Exception {
		JGroupPartitionProcessor p = new JGroupPartitionProcessor();
		final DataProcessor p1 = context.mock(DataProcessor.class);
		List<DataProcessor> partitionProcessors = new ArrayList<DataProcessor>();
		partitionProcessors.add(p1);
		p.setPartitionProcessors(partitionProcessors);
		final ProcessorContext ctx = this.getContext();
		context.checking(new Expectations() {
			{
				one(p1).beforeProcess(ctx);
				one(p1).process(ctx);
				one(p1).afterProcess(ctx);
			}
		});
		p.process(ctx);
	}

	private ProcessorContext getContext() throws Exception {
		List<BeanA> list = new ArrayList<BeanA>();
		for (int i = 0; i < 10; i++) {
			BeanA test = new BeanA();
			test.setId(i);
			test.setName("" + i);
			list.add(test);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("list", list);
		return context;
	}
}

// $Id$