/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * 用来记录<code>Task</code>,<code>State</code>,<code>Event</code>和
 * <code>Session</code>的变量
 * 
 * @author wei.zhang
 * 
 */
@Table(name = "FSM_VARIABLE")
@Entity
public class Variable implements Serializable {

	private final static Log log = LogFactory.getLog(Variable.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -8094637349840264860L;

	/**
	 * 数据库id
	 */
	private Long id;

	/**
	 * 变量名
	 */
	private String name;

	/**
	 * 变量在内存中的数值
	 */
	private Serializable value;

	/**
	 * 变量保存到数据库中的数值
	 */
	private String persistentValue;

	/**
	 * 变量类型
	 */
	private String classType;

	/**
	 * 变量的修改时间
	 */
	private Date changeDate;

	/**
	 * 如果该变量是task相关的则该属性不能为空
	 */
	private Task task;

	/**
	 * 如果该变量是session相关的则该属性不能为空
	 */
	private Session session;

	/**
	 * 如果该变量是event相关的则该属性不能为空
	 */
	private Event event;

	/**
	 * 如果该变量是state相关的则该属性不能为空
	 */
	private State state;

	private Set<Detail> detailSet;

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

	@Transient
	public Serializable getValue() {
		if (value == null) {
			String pValue = this.getPersistentValue();
			if (pValue == null) {
				this.value = null;
				return this.value;
			}
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Class<?> clazz = Class.forName(classType);
				if (clazz.equals(Long.class)) {
					this.value = Long.parseLong(pValue);
				} else if (clazz.equals(Integer.class)) {
					this.value = Integer.parseInt(pValue);
				} else if (clazz.equals(Short.class)) {
					this.value = Integer.parseInt(pValue);
				} else if (clazz.equals(Float.class)) {
					this.value = Integer.parseInt(pValue);
				} else if (clazz.equals(Double.class)) {
					this.value = Integer.parseInt(pValue);
				} else if (clazz.equals(Byte.class)) {
					this.value = Integer.parseInt(pValue);
				} else if (clazz.equals(String.class)) {
					this.value = pValue;
				} else if (Date.class.isAssignableFrom(clazz)) {
					this.value = sdf.parse(pValue);
				} else if (Calendar.class.isAssignableFrom(clazz)) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(sdf.parse(pValue));
					this.value = cal;
				} else if (clazz.equals(Boolean.class)) {
					this.value = Boolean.parseBoolean(pValue);
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return value;
	}

	public void setValue(Serializable value) {
		this.value = value;
		if (value == null) {
			this.persistentValue = null;
			this.classType = null;
			return;
		}
		this.classType = value.getClass().getName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if (value instanceof Date) {
			this.persistentValue = sdf.format((Date) value);
		} else if (value instanceof Calendar) {
			this.persistentValue = sdf.format(((Calendar) value).getTime());
		} else if (value instanceof Number) {
			this.persistentValue = value.toString();
		} else if (value instanceof String) {
			this.persistentValue = value.toString();
		} else {
			this.persistentValue = value.toString();
		}
	}

	@Column(name = "VALUE", length = 4000)
	public String getPersistentValue() {
		return persistentValue;
	}

	public void setPersistentValue(String persistentValue) {
		this.persistentValue = persistentValue;
	}

	@Column(name = "CHANGE_DATE")
	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "TASK_ID", nullable = true)
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "SESSION_ID", nullable = true)
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "EVENT_ID", nullable = true)
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "STATE_ID", nullable = true)
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@OneToMany(mappedBy = "variable")
	@Cascade(value = { CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	public Set<Detail> getDetailSet() {
		return detailSet;
	}

	public void setDetailSet(Set<Detail> detailSet) {
		this.detailSet = detailSet;
	}

	@Column(name = "CLASS_TYPE")
	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1659294399, 439827133).append(this.name)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Variable)) {
			return false;
		}
		Variable rhs = (Variable) object;
		return new EqualsBuilder().append(this.name, rhs.name).isEquals();
	}

}

// $Id$