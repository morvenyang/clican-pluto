/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;

import com.clican.pluto.common.schedule.TaskScheduler;
import com.clican.pluto.common.schedule.TaskSchedulerImpl;
import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.engine.DataProcessTransaction;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.engine.processes.TimerProcessor;

public class TimerProcesssorTestCase extends BaseDplTestCase {

	private Mockery context = new Mockery();

	private TaskScheduler taskScheduler = new TaskSchedulerImpl();

	private DataProcessTransaction dataProcessTransaction;

	public void setDataProcessTransaction(DataProcessTransaction dataProcessTransaction) {
		this.dataProcessTransaction = dataProcessTransaction;
	}

	public void test() throws Exception {
		taskScheduler.start();
		TimerProcessor p = new TimerProcessor();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date start = DateUtils.add(new Date(), Calendar.SECOND, 3);
		Date end = DateUtils.add(start, Calendar.SECOND, 3);
		p.setStartTime(sdf.format(start));
		p.setEndTime(sdf.format(end));
		p.setCronExpression("* * * * * ?");
		p.setConcurrent(true);
		p.setTaskScheduler(taskScheduler);
		p.setDataProcessTransaction(dataProcessTransaction);
		List<DataProcessor> timerProcessors = new ArrayList<DataProcessor>();
		final DataProcessor dp = context.mock(DataProcessor.class);
		timerProcessors.add(dp);
		p.setTimerProcessors(timerProcessors);
		final ProcessorContext ctx = new ProcessorContextImpl();
		context.checking(new Expectations() {
			{
				atLeast(2).of(dp).beforeProcess(ctx);
				atLeast(2).of(dp).process(ctx);
				atLeast(2).of(dp).afterProcess(ctx);
			}
		});
		p.process(ctx);
		taskScheduler.shutdown();
	}

	
	protected void onTearDown() throws Exception {
		this.applicationContext.close();
	}

}

// $Id$