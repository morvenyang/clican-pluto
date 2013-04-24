/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.listener;

import com.clican.pluto.common.calendar.BusinessCalendar;
import com.clican.pluto.fsm.model.Job;

/**
 * 
 * 
 * @author wei.zhang
 * 
 */
public interface TimeOutListener {

	/**
	 * 当超时时该listener被调用
	 * 
	 * @param job
	 */
	public void onTimeOut(Job job);

	/**
	 * 返回起始时间
	 * 
	 * @return
	 */
	public String getStartTime();

	/**
	 * 返回当前listener的执行时间描述字符串
	 * 
	 * @return
	 */
	public String getDueTime();

	/**
	 * 当前listener执行次数的描述字符串
	 * 
	 * @return
	 */
	public String getRepeatTime();

	/**
	 * 重复执行的时间间隔
	 * 
	 * @return
	 */
	public String getRepeatDuration();

	/**
	 * 返回该Timeout对应的<code>BusinessCalendar</code>
	 * 
	 * @return
	 */
	public BusinessCalendar getBusinessCalendar();

	/**
	 * 返回该Timeout对应的<code>BusinessCalendar</code>的spring中的bean的name
	 * 
	 * @return
	 */
	public String getBusinessCalendarName();
}

// $Id$