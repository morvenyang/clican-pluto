/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.control;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.clican.pluto.common.exception.PlutoException;
import com.clican.pluto.common.inter.CloneBean;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.common.util.BeanUtils;
import com.clican.pluto.common.util.StringUtils;

public abstract class Control implements Serializable, CloneBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4970511055279285323L;

	private boolean nullable;

	private boolean supportMutil = false;

	private boolean dynamic;

	public abstract String getName();

	public abstract Control newControl();

	public abstract List<Type> getSupportTypeList();

	public boolean isSupportMutil() {
		return supportMutil;
	}

	public void setSupportMutil(boolean supportMutil) {
		this.supportMutil = supportMutil;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public String toString() {
		return getName();
	}

	public static Control decodeProperty(String description) {
		Map<String, String> properties = new HashMap<String, String>();
		for (String s : description.split(";")) {
			String[] entry = s.split("=");
			if (entry.length == 1) {
				properties.put(entry[0], null);
			} else {
				properties.put(entry[0], entry[1]);
			}
		}
		Control control = null;
		if (properties.get("class") != null) {
			try {
				control = (Control) Class.forName(properties.get("class"))
						.newInstance();
			} catch (Exception e) {
				throw new PlutoException(e);
			}
		} else {
			throw new PlutoException(
					"Cannot get class property from dymanic property's control annotation.");
		}
		properties.remove("class");
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
				continue;
			}
			try {
				org.apache.commons.beanutils.BeanUtils.setProperty(control,
						key, value);
			} catch (Exception e) {
				List<String> listValue = StringUtils
						.getListFromSymbolSplitString(value, ",");
				try {
					org.apache.commons.beanutils.BeanUtils.setProperty(control,
							key, listValue);
				} catch (Exception ex) {
					throw new PlutoException(ex);
				}
			}
		}
		return control;
	}

	public String codecProperty() {
		StringBuffer str = new StringBuffer();
		for (Method method : getClass().getMethods()) {
			String methodName = method.getName();
			if ((methodName.startsWith("get") || methodName.startsWith("is"))
					&& !methodName.equals("getName")
					&& !methodName.equals("getSupportTypeList")) {
				String propertyName = StringUtils
						.getPropertyNameByGetMethodName(methodName);
				str.append(propertyName);
				str.append("=");
				try {
					Object propertyValue;
					if (methodName.equals("getClass")) {
						propertyValue = getClass().getName();
					} else {
						propertyValue = BeanUtils.getProperty(this,
								propertyName);
					}
					if (propertyValue != null) {
						if (propertyValue.getClass().isArray()) {
							for (Object obj : (Object[]) propertyValue) {
								str.append(obj);
								str.append(",");
							}
						} else if (propertyValue instanceof Collection) {
							for (Object obj : (Collection<?>) propertyValue) {
								str.append(obj);
								str.append(",");
							}
						} else {
							str.append(propertyValue);
						}

					}
					str.append(";");
				} catch (Exception e) {
					throw new PlutoException(e);
				}
			}
		}
		return str.toString();
	}

	public Control getCloneBean() {
		try{
			return (Control)org.apache.commons.beanutils.BeanUtils.cloneBean(this);
		}catch(Exception e){
			throw new PlutoException(e);
		}
	}



	public static void main(String[] args) {
		SelectOneControl c = new SelectOneControl();
		System.out.println(c.codecProperty());
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Control)) {
			return false;
		}
		Control rhs = (Control) object;
		return new EqualsBuilder().append(this.dynamic, rhs.dynamic).append(
				this.supportMutil, rhs.supportMutil).append(this.nullable,
				rhs.nullable).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1269076493, -528454811)
				.append(this.dynamic).append(this.supportMutil).append(
						this.nullable).toHashCode();
	}
}

// $Id$