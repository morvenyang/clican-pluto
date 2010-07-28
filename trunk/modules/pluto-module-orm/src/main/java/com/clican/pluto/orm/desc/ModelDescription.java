/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.orm.desc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.InputTextControl;
import com.clican.pluto.common.inter.CloneBean;
import com.clican.pluto.common.type.StringType;
import com.clican.pluto.common.type.Type;

public class ModelDescription implements CloneBean {

	private String name;

	private List<PropertyDescription> propertyDescriptionList;

	public ModelDescription() {
		this.propertyDescriptionList = new ArrayList<PropertyDescription>();
		PropertyDescription pd = new PropertyDescription();
		pd.setName("name");
		pd.setType(new StringType());
		pd.setControl(new InputTextControl());
		propertyDescriptionList.add(pd);
	}
	
	public ModelDescription(String name,String[] propertyNames){
		this.name = name;
		this.propertyDescriptionList = new ArrayList<PropertyDescription>();
		PropertyDescription pd = new PropertyDescription();
		pd.setName("name");
		propertyDescriptionList.add(pd);
		for (int i = 0; i < propertyNames.length; i++) {
			pd = new PropertyDescription();
			pd.setName(propertyNames[i]);
			propertyDescriptionList.add(pd);
		}
	}

	public ModelDescription(String name, String[] propertyNames,
			Type[] propertyTypes,Control[] controls) {
		this.name = name;
		this.propertyDescriptionList = new ArrayList<PropertyDescription>();
		PropertyDescription pd = new PropertyDescription();
		pd.setName("name");
		propertyDescriptionList.add(pd);
		for (int i = 0; i < propertyNames.length; i++) {
			pd = new PropertyDescription();
			pd.setName(propertyNames[i]);
			pd.setType(propertyTypes[i]);
			pd.setControl(controls[i]);
			propertyDescriptionList.add(pd);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstCharUpperName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String getFirstCharLowerName() {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	public List<PropertyDescription> getPropertyDescriptionList() {
		return propertyDescriptionList;
	}

	public void setPropertyDescriptionList(
			List<PropertyDescription> propertyDescriptionList) {
		this.propertyDescriptionList = propertyDescriptionList;
	}

	public ModelDescription getCloneBean() {
		ModelDescription md = new ModelDescription();
		md.setName(name);
		md.getPropertyDescriptionList().clear();
		for (PropertyDescription pd : propertyDescriptionList) {
			md.getPropertyDescriptionList().add(pd.getCloneBean());
		}
		return md;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("firstCharLowerName",
				this.getFirstCharLowerName()).append("name", this.name).append(
				"firstCharUpperName", this.getFirstCharUpperName()).append(
				"propertyDescriptionList", this.propertyDescriptionList)
				.toString();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof ModelDescription)) {
			return false;
		}
		ModelDescription rhs = (ModelDescription) object;
		return new EqualsBuilder().append(this.name, rhs.name).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(2126934477, 1969797363).append(this.name)
				.toHashCode();
	}

}

// $Id: ModelDescription.java 577 2009-06-17 06:30:38Z clican $