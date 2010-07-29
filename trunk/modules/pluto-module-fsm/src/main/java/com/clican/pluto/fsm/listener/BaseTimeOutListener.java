/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.listener;

import com.clican.pluto.common.calendar.BusinessCalendar;

/**
 * Timeout event's base class implementation, it provides the basic timeout
 * parameter configuration.
 * <p>
 * 超时事件的基本实现类,提供了基础的超时参数的设置.
 * 
 * @author clican
 * 
 */
public abstract class BaseTimeOutListener extends BaseListener implements
		TimeOutListener {

	/**
	 * Start time's variable name and this parameter can't be constant value. If
	 * the dueTime is time point, this parameter will not be effective. If the
	 * dueTime is time duration, the due time will base on this start time.
	 * <p>
	 * 起始时间的变量名并且该参数不支持常量. 如果dueTime是一个时间点,那么该变量无效.
	 * 如果逾期时间是一个时间段,那么其延迟的时间就是基于startTime的时间
	 */
	private String startTime;
	/**
	 * Due time's variable name or the constant value. For example: <li>
	 * constant value: dueTime="'1 business hour'" <li> <li>
	 * variable name: dueTime="dueTimeVar" <li>
	 * <p>
	 * 逾期时间的变量名或常量 例如:
	 * <li>
	 * 常量: dueTime="'1 business hour'"
	 * <li>
	 * <li>
	 * 变量名: dueTime="dueTimeVar"
	 * <li>
	 */
	private String dueTime;

	/**
	 * If the repeatTime<=0, this variable will be disable. If the
	 * repeatTime>=1, this variable will define the repeat duration to trigger
	 * this time out listener. If the user wants to change the repeatDuration
	 * return value by programming, you just need override the
	 * <code>getRepeatDuration</code>.
	 * <p>
	 * 如果repeatTime<=0该变量无效,如果repearTime>=1,那么该变量就定义了重复触发该TimeoutListener的时间间隔.
	 * 如果需要编程式的实现repeatDuration的返回数值之需要覆盖本基本的<code>getRepeatDuration()</code>
	 * 的实现就可以了.
	 * 
	 */
	private String repeatDuration;

	/**
	 * repeat times
	 * <p>
	 * 重复次数的变量名
	 */
	private String repeatTime;

	/**
	 * The name of this timeout listener.
	 * 
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