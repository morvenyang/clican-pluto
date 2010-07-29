/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.common.calendar.BusinessCalendar;

public abstract class BaseTimeOutListener implements TimeOutListener {

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * 如果逾期时间是一个时间段,那么就是基于该时间的一个时间段
	 */
	private String startTime;
	/**
	 * 逾期时间的变量名
	 */
	private String dueTime;

	/**
	 * 重复时间间隔的变量名
	 */
	private String repeatDuration;

	/**
	 * 重复次数的变量名
	 */
	private String repeatTime;

	/**
	 * 该Timeout的名称
	 */
	private String name;

	private String businessCalendarName;

	private BusinessCalendar businessCalendar;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getDueTime() {
		return dueTime;
	}

	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
	}

	public String getRepeatDuration() {
		return repeatDuration;
	}

	public void setRepeatDuration(String repeatDuration) {
		this.repeatDuration = repeatDuration;
	}

	public String getRepeatTime() {
		return repeatTime;
	}

	public void setRepeatTime(String repeatTime) {
		this.repeatTime = repeatTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BusinessCalendar getBusinessCalendar() {
		return businessCalendar;
	}

	public void setBusinessCalendar(BusinessCalendar businessCalendar) {
		this.businessCalendar = businessCalendar;
	}

	public String getBusinessCalendarName() {
		return businessCalendarName;
	}

	public void setBusinessCalendarName(String businessCalendarName) {
		this.businessCalendarName = businessCalendarName;
	}

}

// $Id$