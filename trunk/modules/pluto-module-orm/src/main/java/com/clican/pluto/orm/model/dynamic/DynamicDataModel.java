/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.orm.model.dynamic;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DynamicDataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1466711522321962851L;

	private Long id;

	private Map<?, ?> userAttributes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<?, ?> getUserAttributes() {
		return userAttributes;
	}

	public void setUserAttributes(Map<?, ?> userAttributes) {
		this.userAttributes = userAttributes;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"userAttributes", this.userAttributes).toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-164707403, 383581487).append(
				this.userAttributes).append(this.id).toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof DynamicDataModel)) {
			return false;
		}
		DynamicDataModel rhs = (DynamicDataModel) object;
		return new EqualsBuilder().append(this.userAttributes,
				rhs.userAttributes).append(this.id, rhs.id).isEquals();
	}

}

// $Id: DynamicDataModel.java 491 2009-06-09 03:20:17Z clican $