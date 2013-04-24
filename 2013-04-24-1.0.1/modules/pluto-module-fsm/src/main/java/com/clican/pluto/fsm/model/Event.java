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
 * 每个State都会处理若干的Event, 每个Event事件都会被保存到数据库中。
 * 
 * @author wei.zhang
 * 
 */
@Table(name = "FSM_EVENT")
@Entity
public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4596288562036454493L;

	/**
	 * 数据库id
	 */
	private Long id;

	/**
	 * 该Event对象的变量
	 */
	private Set<Variable> variableSet;

	/**
	 * 该Event所属于的状态基
	 */
	private State state;

	/**
	 * 该Event实际结束的时间
	 */
	private Date completeTime;

	/**
	 * Event的类型
	 * 
	 * @see com.clican.pluto.fsm.enumeration.EventType
	 */
	private String eventType;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "event")
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

	@Column(name = "COMPLETE_TIME")
	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	@Column(name = "EVENT_TYPE")
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

}

// $Id$