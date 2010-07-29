/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * 状态节点的具体描述类
 * 
 * @author wei.zhang
 * 
 */
@Table(name = "FSM_STATE")
@Entity
public class State implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4604689554818254665L;

	/**
	 * 数据库id
	 */
	private Long id;

	/**
	 * 状态基的名称
	 */
	private String name;

	/**
	 * 状态基的值
	 */
	private Integer value;

	/**
	 * 当前状态基的状态
	 * 
	 * @see com.clican.pluto.fsm.enumeration.Status
	 */
	private String status;

	/**
	 * 当前状态基所属于的Session
	 */
	private Session session;

	/**
	 * 当前状态基下的Job集合
	 */
	private Set<Job> jobSet;

	/**
	 * 当前状态基下的任务集合
	 */
	private Set<Task> taskSet;

	/**
	 * 当前状态基下的事件集合
	 */
	private Set<Event> eventSet;

	/**
	 * 当前状态基下的变量集合
	 */
	private Set<Variable> variableSet;

	/**
	 * 状态基开始时间
	 */
	private Date startTime;

	/**
	 * 状态基结束时间
	 */
	private Date endTime;

	private int previousStateNumber = 1;

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

	@Column(name = "VALUE")
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name = "SESSION_ID")
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@OneToMany(mappedBy = "state")
	@Cascade(value = { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	public Set<Job> getJobSet() {
		return jobSet;
	}

	public void setJobSet(Set<Job> jobSet) {
		this.jobSet = jobSet;
	}

	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@OneToMany(mappedBy = "state")
	@Cascade(value = { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	public Set<Task> getTaskSet() {
		return taskSet;
	}

	public void setTaskSet(Set<Task> taskSet) {
		this.taskSet = taskSet;
	}

	@OneToMany(mappedBy = "state")
	@Cascade(value = { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	public Set<Event> getEventSet() {
		return eventSet;
	}

	public void setEventSet(Set<Event> eventSet) {
		this.eventSet = eventSet;
	}

	@OneToMany(mappedBy = "state")
	@Cascade(value = { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	public Set<Variable> getVariableSet() {
		return variableSet;
	}

	public void setVariableSet(Set<Variable> variableSet) {
		this.variableSet = variableSet;
	}

	@Column(name = "PREVIOUS_STATE_NUMBER")
	public int getPreviousStateNumber() {
		return previousStateNumber;
	}

	public void setPreviousStateNumber(int previousStateNumber) {
		this.previousStateNumber = previousStateNumber;
	}

}

// $Id$