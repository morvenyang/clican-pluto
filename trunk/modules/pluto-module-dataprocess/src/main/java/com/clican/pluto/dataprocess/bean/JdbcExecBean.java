/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.bean;

public class JdbcExecBean extends ExecBean {

	/**
	 * 需要执行的sql
	 */
	private String sql;

	/**
	 * 执行后返回的结果集合需要转换为clazz制定的类型, 如果该值为空则返回默认的Map<String,Object>
	 */
	private Class<?> clazz;

	/**
	 * 是否返回单行记录
	 */
	private boolean singleRow;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public boolean isSingleRow() {
		return singleRow;
	}

	public void setSingleRow(boolean singleRow) {
		this.singleRow = singleRow;
	}

}

// $Id: JdbcExecBean.java 16254 2010-07-16 08:29:32Z wei.zhang $