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
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.common.schedule.ScheduledTask;
import com.clican.pluto.common.schedule.TaskScheduler;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;
import com.clican.pluto.dataprocess.exception.InterruptedException;

/**
 * 定时处理节点，该节点使用Quartz的CornExpression来调度
 * 
 * @author wei.zhang
 * 
 */
public class TimerProcessor extends BaseDataProcessor {

	private List<DataProcessor> timerProcessors;

	private TaskScheduler taskScheduler;

	private String cronExpression;

	private String startTime;

	private String endTime;

	private boolean concurrent;

	private Thread currentThread;

	private boolean stepCommit;

	public void setTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	public void setTimerProcessors(List<DataProcessor> timerProcessors) {
		this.timerProcessors = timerProcessors;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public boolean isConcurrent() {
		return concurrent;
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	public void setStepCommit(boolean stepCommit) {
		this.stepCommit = stepCommit;
	}

	public void destroy() {
		super.destroy();
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

	@Override
	public void process(final ProcessorContext context) throws DataProcessException {
		String id = UUID.randomUUID().toString();
		Date start = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		Date end = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		final List<Throwable> exceptionList = new ArrayList<Throwable>();
		final Thread currentThread = Thread.currentThread();
		try {
			start = sdf2.parse(sdf1.format(start) + " " + startTime.trim());
			end = sdf2.parse(sdf1.format(end) + " " + endTime.trim());
			ScheduledTask task = new ScheduledTask(new Runnable() {
				@Override
				public void run() {
					log.debug("调用timerProcessor前,当前时间是：" + sdf2.format(new Date()));
					try {
						for (DataProcessor timerProcessor : timerProcessors) {
							if (stepCommit) {
								dataProcessTransaction.doInCommit(timerProcessor, context);
							} else {
								timerProcessor.beforeProcess(context);
								timerProcessor.process(context);
								timerProcessor.afterProcess(context);
							}
						}
					} catch (InterruptedException e) {
						log.error("调用Timer出错", e);
						exceptionList.add(e);
						synchronized (currentThread) {
							currentThread.notify();
						}
					} catch (Throwable e) {
						log.error("调用Timer出错", e);
						exceptionList.add(e);
					}
					log.debug("调用timerProcessor后,当前时间是：" + sdf2.format(new Date()));
				}
			}, id, cronExpression, start, end, concurrent);
			long timeout = end.getTime() - new Date().getTime();
			if (log.isDebugEnabled()) {
				log.debug("timeout=" + timeout);
			}
			long beforetime = start.getTime() - new Date().getTime();
			if (beforetime > 5000) {
				throw new InterruptedException("不需要启动task,beforetime=[" + beforetime + "]");
			}
			if (timeout < 0) {
				throw new InterruptedException("当前启动时间不能启动task,timeout=[" + timeout + "]");
			} else {
				taskScheduler.schedule(id, task);
				synchronized (currentThread) {
					currentThread.wait(timeout);
				}
			}
		} catch (Exception e) {
			throw new DataProcessException("Timer定时任务错误[" + this.getId() + "]", e);
		}
		try {
			if (exceptionList.size() != 0) {
				throw new DataProcessException(exceptionList.get(0));
			}
		} catch (Exception e) {
			throw new DataProcessException("执行TimerProcessor[" + this.getId() + "]出错", e);
		} finally {
			taskScheduler.cancel(id);
		}
	}

}

// $Id$