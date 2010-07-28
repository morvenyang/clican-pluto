/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.lang.time.DateUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;

import com.clican.pluto.dataprocess.engine.DataProcessTransaction;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.engine.processes.ForProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

public class ForProcessorTestCase extends TestCase {

	private Mockery context = new Mockery();

	private List<DataProcessor> getIteratorProcessors() {
		DataProcessor p = new DataProcessor() {
			@Override
			public DataProcessor afterProcess(ProcessorContext context) throws DataProcessException {
				return null;
			}

			@Override
			public void beforeProcess(ProcessorContext context) throws DataProcessException {

			}

			@Override
			public String getTransaction() {
				return null;
			}

			@Override
			public void process(ProcessorContext context) throws DataProcessException {

			}
		};
		List<DataProcessor> result = new ArrayList<DataProcessor>();
		result.add(p);
		return result;
	}

	public void test1() throws Exception {
		ForProcessor p = new ForProcessor();
		p.setIteratorProcessors(this.getIteratorProcessors());
		p.setStart("start+1");
		p.setEnd("end+1");
		p.setStep("1");
		ProcessorContext ctx = new ProcessorContextImpl();
		ctx.setAttribute("start", 10);
		ctx.setAttribute("end", 20);
		p.process(ctx);
	}

	public void test2() throws Exception {
		ForProcessor p = new ForProcessor();
		final DataProcessTransaction t = context.mock(DataProcessTransaction.class);
		p.setDataProcessTransaction(t);
		p.setIteratorProcessors(this.getIteratorProcessors());
		p.setStart("start-1");
		p.setEnd("end-1");
		p.setStep("1");
		p.setStepCommit(true);
		final ProcessorContext ctx = new ProcessorContextImpl();
		ctx.setAttribute("start", 10);
		ctx.setAttribute("end", 20);
		context.checking(new Expectations() {
			{
				this.allowing(t).doInCommit((DataProcessor) with(anything()), with(same(ctx)));
			}
		});
		p.process(ctx);
	}
	
	public void test3() throws Exception {
		ForProcessor p = new ForProcessor();
		p.setIteratorProcessors(this.getIteratorProcessors());
		p.setStart("start+1day");
		p.setEnd("end+1day");
		p.setStep("1day");
		ProcessorContext ctx = new ProcessorContextImpl();
		ctx.setAttribute("start", new Date());
		ctx.setAttribute("end", DateUtils.add(new Date(), Calendar.DAY_OF_MONTH, 10));
		p.process(ctx);
	}
	
	public void test4() throws Exception {
		ForProcessor p = new ForProcessor();
		final DataProcessTransaction t = context.mock(DataProcessTransaction.class);
		p.setDataProcessTransaction(t);
		p.setIteratorProcessors(this.getIteratorProcessors());
		p.setStart("start-1month");
		p.setEnd("end-1month");
		p.setStep("1day");
		p.setStepCommit(true);
		final ProcessorContext ctx = new ProcessorContextImpl();
		ctx.setAttribute("start", new Date());
		ctx.setAttribute("end", DateUtils.add(new Date(), Calendar.DAY_OF_MONTH, 10));
		context.checking(new Expectations() {
			{
				this.allowing(t).doInCommit((DataProcessor) with(anything()), with(same(ctx)));
			}
		});
		p.process(ctx);
	}
}

// $Id$