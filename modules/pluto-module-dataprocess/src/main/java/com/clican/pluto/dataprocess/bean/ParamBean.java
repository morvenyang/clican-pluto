/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.bean;

/**
 * 用于藐视dataprocess:param中的参数
 * 
 * @author wei.zhang
 * 
 */
public class ParamBean {

	/**
	 * 参数名
	 */
	private String paramName;

	/**
	 * 参数值
	 */
	private String paramValue;

	/**
	 * 参数类型
	 */
	private String type;

	/**
	 * 是否覆盖<code>ProcessorContext</code>同名参数的值
	 */
	private boolean override;

	/**
	 * 如果type=date，该参数用于表示时间的模式
	 */
	private String pattern;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}

// $Id: ParamBean.java 16253 2010-07-16 08:28:14Z wei.zhang $