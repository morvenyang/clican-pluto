/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.orm.desc;

import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.InputTextControl;
import com.clican.pluto.common.inter.CloneBean;
import com.clican.pluto.common.type.StringType;
import com.clican.pluto.common.type.Type;

public class PropertyDescription implements CloneBean {

	private String id;

	private String name;

	private Type type = new StringType();

	private Control control = new InputTextControl();

	public PropertyDescription() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstCharLowerName() {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	public String getFirstCharUpperName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Control getControl() {
		return control;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("name", this.name).append(
				"control", this.control).append("type", this.type).toString();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof PropertyDescription)) {
			return false;
		}
		PropertyDescription rhs = (PropertyDescription) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.name,
				rhs.name).append(this.type, rhs.type).append(this.control,
				rhs.control).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(544707901, -77129803).append(this.id)
				.append(this.name).append(this.type).append(this.control)
				.toHashCode();
	}

	public PropertyDescription getCloneBean() {
		PropertyDescription pd = new PropertyDescription();
		pd.setControl(control.getCloneBean());
		pd.setType(type.getCloneBean());
		pd.setName(name);
		pd.setId(id);
		return pd;
	}

}

// $Id: PropertyDescription.java 569 2009-06-16 08:21:49Z clican $