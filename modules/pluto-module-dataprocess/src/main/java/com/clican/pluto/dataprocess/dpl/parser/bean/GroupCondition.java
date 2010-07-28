/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 用来保存分组条件的临时对象
 * 
 * @author wei.zhang
 * 
 */
public class GroupCondition implements Comparable<GroupCondition> {

	/**
	 * 分组的名 比如groupName=name
	 */
	private String groupName;

	/**
	 * 分组的数值 比如groupValue=zhangwei或groupValue=zhangdapeng
	 */
	private Object groupValue;

	private int position;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Object getGroupValue() {
		return groupValue;
	}

	public void setGroupValue(Object groupValue) {
		this.groupValue = groupValue;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof GroupCondition)) {
			return false;
		}
		GroupCondition rhs = (GroupCondition) object;
		return new EqualsBuilder().append(this.groupName, rhs.groupName).append(this.groupValue, rhs.groupValue).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(797225563, 1589041477).append(this.groupName).append(this.groupValue).toHashCode();
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(GroupCondition object) {
		GroupCondition myClass = (GroupCondition) object;
		if (this.groupValue instanceof Comparable && myClass.groupValue instanceof Comparable) {
			return new CompareToBuilder().append(this.position, myClass.position).append(this.groupValue, myClass.groupValue).toComparison();
		} else {
			return 0;
		}

	}

}

// $Id: GroupCondition.java 12410 2010-05-13 06:55:57Z wei.zhang $