/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;

import com.clican.pluto.dataprocess.engine.DataProcessTransaction;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.engine.processes.ConditionProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

public class ConditionProcessorTestCase extends TestCase {

	private Mockery context = new Mockery();

	public void test() throws Exception {
		ConditionProcessor p = new ConditionProcessor();
		Map<String, DataProcessor> dataProcessorMap = new HashMap<String, DataProcessor>();
		Map<String, String> exceptionMap = new HashMap<String, String>();
		final DataProcessor p1 = context.mock(DataProcessor.class, "d1");
		final DataProcessor p2 = context.mock(DataProcessor.class, "d2");
		final DataProcessTransaction t = context.mock(DataProcessTransaction.class);
		p.setDataProcessTransaction(t);
		dataProcessorMap.put("a==1", p1);
		dataProcessorMap.put("a==2", p2);
		exceptionMap.put("a==3", "错误");
		p.setDataProcessorMap(dataProcessorMap);
		p.setExceptionMap(exceptionMap);
		final ProcessorContext ctx = new ProcessorContextImpl();
		ctx.setAttribute("a", 2);
		context.checking(new Expectations() {
			{
				one(p2).getTransaction();
				will(new CustomAction("") {
					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						return null;
					}
				});
				one(p2).beforeProcess(ctx);
				one(p2).process(ctx);
				one(p2).afterProcess(ctx);
			}
		});
		p.afterProcess(ctx);

		ctx.setAttribute("a", 1);
		context.checking(new Expectations() {
			{
				one(p1).getTransaction();
				will(new CustomAction("") {
					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						return "begin";
					}
				});
				one(t).doInCommit(p1, ctx);
				will(new CustomAction("") {
					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						return p1;
					}
				});
				one(p1).afterProcess(ctx);
			}
		});
		p.afterProcess(ctx);

		ctx.setAttribute("a", 3);
		try {
			p.afterProcess(ctx);
			fail("应该出错");
		} catch (DataProcessException e) {

		}

	}
}

// $Id$