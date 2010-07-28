/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.testbean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BeanC {
	
	private Integer id;

	private String name;
	
	private Integer testbId;

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

	public Integer getTestbId() {
		return testbId;
	}

	public void setTestbId(Integer testbId) {
		this.testbId = testbId;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1720865377, -1166918691).append(this.id).append(this.testbId).append(this.name).toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof BeanC)) {
			return false;
		}
		BeanC rhs = (BeanC) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.testbId, rhs.testbId).append(this.name, rhs.name)
				.isEquals();
	}

	
	
	
}

// $Id$