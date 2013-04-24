/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

/**
 * 在解析过程中用到的列的对象
 * 
 * @author clican
 * 
 */
public class Column {

	private PrefixAndSuffix prefixAndSuffix;

	/**
	 * 被Select出来后在Map中保存的列名
	 */
	private String columnName;
	
	public Column(PrefixAndSuffix prefixAndSuffix, String columnName) {
		this.prefixAndSuffix = prefixAndSuffix;
		this.columnName = columnName;
	}

	
	public PrefixAndSuffix getPrefixAndSuffix() {
		return prefixAndSuffix;
	}

	public void setPrefixAndSuffix(PrefixAndSuffix prefixAndSuffix) {
		this.prefixAndSuffix = prefixAndSuffix;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	
}

// $Id: Column.java 12410 2010-05-13 06:55:57Z wei.zhang $