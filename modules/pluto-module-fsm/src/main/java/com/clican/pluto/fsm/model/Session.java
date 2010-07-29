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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * 工作流的<code>Session</code>对象。主要描述了该工作流的状态节点和一些变量参数。
 * 
 * @author wei.zhang
 * 
 */
@Table(name = "FSM_SESSION")
@Entity
public class Session implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8005901940029497559L;

	/**
	 * 数据库id
	 */
	private Long id;

	/**
	 * 工作流的名称
	 */
	private String name;

	/**
	 * 工作流对应的版本
	 */
	private int version;

	/**
	 * 工作流的发起者
	 */
	private String sponsor;

	/**
	 * 工作流的开始时间
	 */
	private Date startTime;

	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 已经处理过的状态
	 */
	private Set<State> stateSet;

	/**
	 * 当前Session的变量集合
	 */
	private Set<Variable> variableSet;

	/**
	 * 当前Session的状态
	 * 
	 * @see com.clican.pluto.fsm.enumeration.Status
	 */
	private String status;

	private Session parent;

	private Set<Session> children;

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

	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "LAST_UPDATE_TIME")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@OneToMany(mappedBy = "session")
	@Cascade(value = { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	public Set<State> getStateSet() {
		return stateSet;
	}

	public void setStateSet(Set<State> stateSet) {
		this.stateSet = stateSet;
	}

	@OneToMany(mappedBy = "session")
	@Cascade(value = { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	public Set<Variable> getVariableSet() {
		return variableSet;
	}

	public void setVariableSet(Set<Variable> variableSet) {
		this.variableSet = variableSet;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "SPONSOR")
	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "PARENT_ID", nullable = true)
	public Session getParent() {
		return parent;
	}

	public void setParent(Session parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent")
	@Cascade(value = { CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	public Set<Session> getChildren() {
		return children;
	}

	public void setChildren(Set<Session> children) {
		this.children = children;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Session)) {
			return false;
		}
		Session rhs = (Session) object;
		return new EqualsBuilder().append(this.id, rhs.id).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-154511651, 61940759).append(this.id)
				.toHashCode();
	}

}

// $Id$