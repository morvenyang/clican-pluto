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
 * 任务的具体描述类
 *
 * @author wei.zhang
 *
 */
@Table(name = "FSM_TASK")
@Entity
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5972949234621095708L;

	/**
	 * 数据库id
	 */
	private Long id;

	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 任务类型，外部系统可以自定义
	 */
	private String type;

	/**
	 * 任务的执行人
	 */
	private String assignee;

	/**
	 * 和该任务相关的变量集合
	 */
	private Set<Variable> variableSet;

	/**
	 * 任务所属的状态基
	 */
	private State state;


	//任务分配时间
	/**
	 * 任务被分配的时间
	 */
	private Date assignTime;

	//实际结束时间
	/**
	 * 任务结束时间
	 */
	private Date completeTime;
	
	//该Task的预计结束时间
	private Date endTime;

	@Column(name="END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

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

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "ASSIGNEE")
	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	@OneToMany(mappedBy = "task")
	@Cascade(value = { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	public Set<Variable> getVariableSet() {
		return variableSet;
	}

	public void setVariableSet(Set<Variable> variableSet) {
		this.variableSet = variableSet;
	}

	@ManyToOne
	@JoinColumn(name = "STATE_ID")
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Column(name = "ASSIGN_TIME")
	public Date getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(Date assignTime) {
		this.assignTime = assignTime;
	}

	@Column(name = "COMPLETE_TIME")
	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

}

// $Id$