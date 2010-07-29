/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.clican.pluto.fsm.enumeration.JobStatus;

/**
 * 定时任务的数据对象类,该类主要描述了定时任务的执行时间，以及是否重复执行和一个重复执行的周期。
 * 
 * @author wei.zhang
 * 
 */
@Table(name = "FSM_JOB")
@Entity
public class Job implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4960943563423779924L;

	/**
	 * 数据库id
	 */
	private Long id;

	/**
	 * Job名称
	 */
	private String name;

	/**
	 * Job执行时间
	 */
	private Date executeTime;

	/**
	 * 重复执行次数
	 */
	private int repeatTime;

	/**
	 * 已经执行了的次数
	 */
	private int repeatedTime;

	/**
	 * 重复的时间间隔，单位毫秒
	 */
	private String repeatDuration;

	/**
	 * 当前Job所属于的状态基
	 */
	private State state;

	/**
	 * 当前Job所处的一个状态
	 */
	private String status = JobStatus.IDLE.getStatus();
	
	private String businessCalendarName;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "EXECUTE_TIME")
	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	@Column(name = "REPEAT_TIME")
	public int getRepeatTime() {
		return repeatTime;
	}

	public void setRepeatTime(int repeatTime) {
		this.repeatTime = repeatTime;
	}

	@Column(name = "REPEATED_TIME")
	public int getRepeatedTime() {
		return repeatedTime;
	}

	public void setRepeatedTime(int repeatedTime) {
		this.repeatedTime = repeatedTime;
	}

	@Column(name = "REPEAT_DURATION")
	public String getRepeatDuration() {
		return repeatDuration;
	}

	public void setRepeatDuration(String repeatDuration) {
		this.repeatDuration = repeatDuration;
	}

	@ManyToOne
	@JoinColumn(name = "STATE_ID")
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="BUSINESS_CALENDAR_NAME")
	public String getBusinessCalendarName() {
		return businessCalendarName;
	}

	public void setBusinessCalendarName(String businessCalendarName) {
		this.businessCalendarName = businessCalendarName;
	}

}

// $Id$