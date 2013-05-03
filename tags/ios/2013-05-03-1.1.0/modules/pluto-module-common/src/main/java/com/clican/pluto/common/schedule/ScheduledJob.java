/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.schedule;



import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ScheduledJob implements Job {
	/*
	 * Hold the log handler
	 */
	private static final Logger log = Logger.getLogger(ScheduledJob.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		Runnable command = (Runnable) jobDetail.getJobDataMap().get("command");
		try {
			command.run();
		} catch (Throwable e) {
			log.warn("Fail to execute a job", e);
		}

	}

}

// $Id$