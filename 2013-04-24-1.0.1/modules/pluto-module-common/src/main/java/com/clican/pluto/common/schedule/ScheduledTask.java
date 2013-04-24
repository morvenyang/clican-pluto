/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.schedule;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents a task which will be scheduled to execute periodically
 */
public class ScheduledTask {

	private Date startTime = null;
	private Date endTime = null;
	private String cronExpression = null;
	private String name = null;
	private Runnable command;
	private boolean concurrent;

	/**
	 * Construct a task object
	 * 
	 * @param command
	 *            the runnable object which will be run at a proper period
	 * @param name
	 *            the task name
	 * @param cronExpression
	 *            a cron expression indicating how to run this task.
	 * @param startTime
	 *            the time this task should be started
	 */
	public ScheduledTask(Runnable command, String name, String cronExpression, Date startTime, Date endTime, boolean concurrent) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.cronExpression = cronExpression;
		this.name = name;
		this.command = command;
		this.concurrent = concurrent;
	}

	public boolean isConcurrent() {
		return concurrent;
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	/**
	 * Get the task to execute.
	 * 
	 * @return the task to execute.
	 */
	public Runnable getCommand() {
		return command;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("command", this.command).append("name", this.name).append("cronExpression", this.cronExpression).append(
				"startTime", this.startTime.getTime()).toString();
	}

	public Date getStartTime() {
		return startTime;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public String getName() {
		return name;
	}

	public Date getEndTime() {
		return endTime;
	}

}

// $Id$
