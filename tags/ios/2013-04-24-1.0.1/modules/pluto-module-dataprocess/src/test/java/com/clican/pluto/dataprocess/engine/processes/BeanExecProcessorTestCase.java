/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

import com.clican.pluto.dataprocess.engine.JavaExecuteBean;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.engine.processes.BeanExecProcessor;

public class BeanExecProcessorTestCase extends TestCase {

	private Mockery context = new Mockery();

	public void test() throws Exception {
		BeanExecProcessor p = new BeanExecProcessor();
		final JavaExecuteBean bean = context.mock(JavaExecuteBean.class);
		p.setBean(bean);
		p.setResultName("aaa");
		final ProcessorContext ctx = new ProcessorContextImpl();
		context.checking(new Expectations() {
			{
				this.allowing(bean).processContext(ctx);
			}
		});
		p.process(ctx);
	}
}

// $Id$