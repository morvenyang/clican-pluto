/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.schedule;

import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobStore;

/**
 * <p>
 * This class is used to manage the scheduled tasks related with delivery
 * message objects via unique id.
 * </p>
 * 
 */
public class TaskSchedulerImpl implements TaskScheduler {

	/*
	 * Hold the log handler
	 */
	private static final Logger log = Logger.getLogger(TaskSchedulerImpl.class);

	private Timer timer = new Timer();

	private Scheduler scheduler = null;
	/**
	 * The number of threads used to execute the schedule tasks
	 */
	private int threadNum = 8;

	public void schedule(TimerTask task, Date time) {
		timer.purge();
		timer.schedule(task, time);
	}

	/**
	 * Schedule a set of task associated with a identified id.
	 * 
	 * @param id
	 *            a string id used to identify this set of tasks
	 * @param tasks
	 *            a set of tasks
	 * @throws ExistenceException
	 *             thrown when the same id exists
	 */
	public synchronized void schedule(String id, ScheduledTask task) {

		if (id == null || task == null) {
			if (log.isDebugEnabled()) {
				log.debug("Can't schedule the null object");
			}
			return;
		}
		if (scheduler == null) {
			throw new RuntimeException("The task scheduler is still not started");
		}
		if (log.isDebugEnabled()) {
			log.debug("try to schedule a set of tasks identified by id[" + id + "]");
		}
		String names[];
		try {
			names = scheduler.getJobNames(id);
			if (names != null && names.length > 0) {
				log.error("Already have a set of tasks identified by id[" + id + "], the new task set will be ignored");
			}
		} catch (SchedulerException e) {
			log.warn(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("try to schedule the task:" + task.toString());
		}
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("command", task.getCommand());
		JobDetail jobDetail = new JobDetail(task.getName(), id, task.isConcurrent() ? StatefulScheduledJob.class : ScheduledJob.class);
		jobDetail.setJobDataMap(dataMap);
		jobDetail.setDescription(task.getName());
		CronTrigger trigger = new CronTrigger();
		trigger.setName(task.getName());
		trigger.setGroup(id);
		trigger.setStartTime(task.getStartTime());
		trigger.setEndTime(task.getEndTime());
		try {
			trigger.setCronExpression(task.getCronExpression());
		} catch (ParseException e) {
			throw new RuntimeException("Error when parsing cron expression.", e);
		}
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			if (log.isDebugEnabled()) {
				log.debug("schedule the task successfully");
			}
		} catch (SchedulerException e) {
			throw new RuntimeException("Error when scheduling the task.", e);
		}

	}

	/**
	 * Cancel the tasks associated with identified id
	 * 
	 * @param id
	 */
	public synchronized void cancel(String id) {

		if (id == null) {
			return;
		}
		if (scheduler == null) {
			log.warn("The task scheduler is still not started");
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("try to cancel a set of tasks identified by id[" + id + "].");
		}
		try {
			String names[] = scheduler.getJobNames(id);
			if (names == null || names.length == 0) {
				if (log.isDebugEnabled()) {
					log.debug("No such tasks");
				}
				return;
			}
			for (String name : names) {
				if (log.isDebugEnabled()) {
					log.debug("delete the job with name[" + name + "]");
				}
				scheduler.deleteJob(name, id);
			}
		} catch (SchedulerException e) {
			log.warn("Get a exception when canceling the tasks", e);
		}

	}

	public void shutdown() {
		// Stop the associated scheduler service.
		if (log.isDebugEnabled()) {
			log.debug("Shutting down The task scheduler service ");
		}
		long shutdownStartTime = System.currentTimeMillis();

		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			log.warn("Failed when stopping the scheduler service", e);
		}
		try {
			timer.cancel();
			timer.purge();
		} catch (Exception e) {
			log.warn("Failed when stopping the timer service", e);
		}
		scheduler = null;

		if (log.isDebugEnabled()) {
			long shutdownEndTime = System.currentTimeMillis();
			log.debug("The scheduler service has properly trminated after " + (shutdownEndTime - shutdownStartTime) + "ms.");
		}

	}

	public void start() {
		if (scheduler != null) {
			if (log.isInfoEnabled()) {
				log.info("The scheduler has been started");
			}
			return;
		}
		try {
			if (log.isDebugEnabled()) {
				log.debug("Using the number[" + this.threadNum + "] of thread to schedule the service");
			}
			SimpleThreadPool threadPool = new SimpleThreadPool(threadNum, Thread.NORM_PRIORITY);
			threadPool.initialize();
			JobStore jobStore = new RAMJobStore();

			String schedulerName = this.getClass().getName();
			DirectSchedulerFactory.getInstance().createScheduler(schedulerName, schedulerName, threadPool, jobStore);
			scheduler = DirectSchedulerFactory.getInstance().getScheduler(schedulerName);

			// Start the scheduler service
			if (log.isDebugEnabled()) {
				log.debug("Try to start the scheduler service");
			}
			scheduler.start();

		} catch (SchedulerException e) {
			scheduler = null;
			throw new RuntimeException("Cannot create scheduler service.", e);
		}

	}

	public int getThreadNum() {
		return threadNum;
	}

	/**
	 * Set the number of threads used for executing the schedule tasks
	 * 
	 * @param threadNum
	 */
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

}

// $Id$
