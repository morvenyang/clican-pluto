/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.testbean;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.clican.pluto.dataprocess.engine.ProcessorContext;

public class BeanA {

	private Integer id;

	private String name;

	private Date date;

	private double doubleValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double value) {
		this.doubleValue = value;
	}

	public void process(ProcessorContext context) {

	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof BeanA)) {
			return false;
		}
		BeanA rhs = (BeanA) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.name, rhs.name).append(this.date, rhs.date).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1129368575, -1371812007).append(this.id).append(this.name).append(this.date).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("date", this.date).append("name", this.name).append("doubleValue", this.doubleValue)
				.toString();
	}

}

// $Id$