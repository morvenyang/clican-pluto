/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 用来描述联合key值中的一个真正的CombinedKey是一个Set<CombinedKey>
 *
 * @author wei.zhang
 *
 */
public class CombinedKey {

	
	private String name;
	
	private Object value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof CombinedKey)) {
			return false;
		}
		CombinedKey rhs = (CombinedKey) object;
		return new EqualsBuilder().append(this.name, rhs.name).append(this.value, rhs.value).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-37543439, -300403667).append(this.name).append(this.value).toHashCode();
	}
	
	
}


//$Id: CombinedKey.java 12410 2010-05-13 06:55:57Z wei.zhang $