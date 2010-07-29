/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.common.calendar.BusinessCalendar;
import com.clican.pluto.common.calendar.Duration;
import com.clican.pluto.common.util.TimeUtils;
import com.clican.pluto.fsm.engine.EngineContext;
import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.engine.JobContext;
import com.clican.pluto.fsm.engine.JobService;
import com.clican.pluto.fsm.enumeration.JobStatus;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.listener.TimeOutListener;
import com.clican.pluto.fsm.model.Job;

/**
 * 该类主要是用来维护Job的执行的。 外部系统通过该类的接口可以新增，删除一个Job对象。
 * <p>
 * 该类本身通过Spring配置的任务调度会定时的从数据库从取出即将执行的任务。 并且把该任务schedule到<code>Timer</code>
 * 对象中去执行。
 * 
 * @author wei.zhang
 * 
 */
public class JobContextImpl implements JobContext, ApplicationContextAware {

	private final static Log log = LogFactory.getLog(JobContextImpl.class);

	private ApplicationContext applicationContext;

	private ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	private BusinessCalendar businessCalendar;

	private int maxThreadNum = 200;

	private AtomicLong activeThreadCount = new AtomicLong();

	private Timer timer = new Timer();

	private JobService jobService;

	private long refreshTime;

	private EngineContext engineContext;

	private boolean running = false;

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = TimeUtils.getMillisionSecond(refreshTime);
	}

	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

	public void setEngineContext(EngineContext engineContext) {
		this.engineContext = engineContext;
	}

	public void setMaxThreadNum(int maxThreadNum) {
		this.maxThreadNum = maxThreadNum;
	}

	public void setBusinessCalendar(BusinessCalendar businessCalendar) {
		this.businessCalendar = businessCalendar;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Transactional
	public void addJob(final Job job) {
		if (log.isDebugEnabled()) {
			log.debug("add a new name=[" + job.getName() + "]");
		}
		// Calendar current = Calendar.getInstance();
		// Date date = job.getExecuteTime();
		// long gap = date.getTime() - current.getTimeInMillis();
		// if (gap < refreshTime * 2) {
		// if (log.isDebugEnabled()) {
		// log.debug("execute the job=[" + job.getId() + "], job hash=" +
		// job.hashCode());
		// }
		// job.setStatus(JobStatus.EXECUTING.getStatus());
		// jobService.saveJob(job);
		//
		// if (log.isDebugEnabled()) {
		// log.debug("check: after saving job id =" + job.getId() +
		// ", job hash=" + job.hashCode());
		// }
		//
		// executor.submit(getRunnable(job));
		// } else {
		if (log.isDebugEnabled()) {
			log.debug("save the job=[" + job.getId() + "] into database queue");
		}
		jobService.saveJob(job);
		// }
	}

	public void loadJobs() {
		if (!running) {
			start();
		}
		if (!running || activeThreadCount.longValue() > maxThreadNum) {
			if (log.isDebugEnabled()) {
				log.debug("Running=[" + running + "],activeThreadCount=["
						+ activeThreadCount.longValue() + "],maxThreadNum=["
						+ maxThreadNum + "] This loading is ignored");
			}
			return;
		}
		Calendar current = Calendar.getInstance();
		current.setTimeInMillis(current.getTimeInMillis() + refreshTime);
		List<Job> jobList = jobService.findJobByExecuteTime(current.getTime(),
				JobStatus.IDLE);
		if (log.isTraceEnabled()) {
			log.trace("find " + jobList.size() + " jobs to execute");
		}
		for (Job job : jobList) {
			job.setStatus(JobStatus.EXECUTING.getStatus());
			jobService.updateJob(job);
			if (log.isDebugEnabled()) {
				log.debug("find a job [" + job + "] and prepare to execute it");
			}
			executor.submit(getRunnable(job));
		}
	}

	@Transactional
	public void removeJob(Job job) {
		jobService.deleteJob(job);
	}

	@Transactional
	public void executeJob(Job job) {
		job = jobService.findJobById(job.getId());
		job.setStatus(JobStatus.EXECUTING.getStatus());
		jobService.updateJob(job);
		IState istate = engineContext.getState(job.getState().getSession()
				.getName(), job.getState().getSession().getVersion(), job
				.getState().getName());
		TimeOutListener listener = istate.getTimeOutListenerMap().get(
				job.getName());
		listener.onTimeOut(job);
	}

	public void start() {
		Calendar current = Calendar.getInstance();
		List<Job> jobList = jobService.findJobByExecuteTime(current.getTime(),
				JobStatus.EXECUTING);
		for (Job job : jobList) {
			executor.submit(getRunnable(job));
		}
		running = true;
	}

	public void stop() {
		running = false;
		timer.cancel();
	}

	private Runnable getRunnable(final Job job) {
		return new Runnable() {

			public void run() {
				activeThreadCount.getAndIncrement();
				TimerTask task = new TimeOutTask(job);
				if (log.isDebugEnabled()) {
					log.debug("prepare to execute this job ["
							+ job.getId()
							+ "] after "
							+ (job.getExecuteTime().getTime() - new Date()
									.getTime()) / 1000 + " seconds");
				}
				timer.schedule(task, job.getExecuteTime());
			}
		};
	}

	private class TimeOutTask extends TimerTask {

		private Job job;

		public TimeOutTask(Job job) {
			this.job = job;
		}

		public void run() {
			try {
				if (log.isDebugEnabled()) {
					log.debug("TimeOutTask: try to execute job" + job.getId());
				}
				if (job.getRepeatTime() - job.getRepeatedTime() < 0) {
					if (log.isDebugEnabled()) {
						log
								.debug("TimeOutTask returns for {job.getRepeatTime() - job.getRepeatedTime() < 0}");
					}
					return;
				}
				job = jobService.findJobById(job.getId());
				if (job == null) {
					if (log.isDebugEnabled()) {
						log.debug("TimeOutTask returns for {job == null}");
					}
					return;
				}
				if (!JobStatus.EXECUTING.getStatus().equals(job.getStatus())) {
					if (log.isDebugEnabled()) {
						log
								.debug("TimeOutTask returns for {!JobStatus.EXECUTING.getStatus().equals(job.getStatus())}");
					}
					return;
				}

				if (Status.convert(job.getState().getStatus()) != Status.ACTIVE) {
					log.debug("The state [" + job.getState().getName() + "("
							+ job.getState().getId()
							+ ")] has been inactived, we ingore this job["
							+ job.getName() + "(" + job.getId() + ")] running");
					return;
				}
				if (Status.convert(job.getState().getSession().getStatus()) != Status.ACTIVE) {
					log.debug("The state ["
							+ job.getState().getSession().getName() + "("
							+ job.getState().getSession().getId()
							+ ")] has been inactived, we ingore this job["
							+ job.getName() + "(" + job.getId() + ")] running");
					return;
				}
				IState istate = engineContext.getState(job.getState()
						.getSession().getName(), job.getState().getSession()
						.getVersion(), job.getState().getName());

				TimeOutListener listener = istate.getTimeOutListenerMap().get(
						job.getName());
				if (log.isDebugEnabled()) {
					log.debug("execute this job [" + job + "] jobId = ["
							+ job.getId() + "]");
				}
				listener.onTimeOut(job);
				job = jobService.findJobById(job.getId());
			} catch (Throwable e) {
				log.error("", e);
			} finally {
				try {
					activeThreadCount.decrementAndGet();
					if (job != null
							&& JobStatus.EXECUTED.getStatus().equals(
									job.getStatus())) {
						jobService.deleteJob(job);
						return;
					}
					if (job != null) {
						if (job.getRepeatTime() - job.getRepeatedTime() <= 0) {
							jobService.deleteJob(job);
						} else {
							job.setRepeatedTime(job.getRepeatedTime() + 1);
							job.setStatus(JobStatus.IDLE.getStatus());
							BusinessCalendar businessCalendar = JobContextImpl.this.businessCalendar;
							if (StringUtils.isNotEmpty(job
									.getBusinessCalendarName())) {
								try {
									businessCalendar = (BusinessCalendar) applicationContext
											.getBean(job
													.getBusinessCalendarName());
								} catch (Exception e) {
									log.error("", e);
								}
							}
							if (StringUtils.isNotEmpty(job.getRepeatDuration())) {
								Date nextExecuteTime = businessCalendar.add(job
										.getExecuteTime(), new Duration(job
										.getRepeatDuration()));
								job.getExecuteTime().setTime(
										nextExecuteTime.getTime());
								jobService.updateJob(job);
							} else {
								jobService.deleteJob(job);
							}
						}
					}
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}
}

// $Id$