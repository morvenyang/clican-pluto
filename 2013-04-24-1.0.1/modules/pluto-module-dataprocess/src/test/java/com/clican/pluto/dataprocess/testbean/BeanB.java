/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.testbean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BeanB {

	private Integer id;
	
	private String name;
	
	private Integer testaId;

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

	public Integer getTestaId() {
		return testaId;
	}

	public void setTestaId(Integer testaId) {
		this.testaId = testaId;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(599513583, -676266319).append(this.id).append(this.testaId).append(this.name).toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof BeanB)) {
			return false;
		}
		BeanB rhs = (BeanB) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.testaId, rhs.testaId).append(this.name, rhs.name)
				.isEquals();
	}
	
	
}


//$Id$