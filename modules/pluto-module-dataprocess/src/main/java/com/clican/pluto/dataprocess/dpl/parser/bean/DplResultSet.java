/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 在多个数据集合Join的过程中产生的临时Join结果集合
 * 
 * @author wei.zhang
 * 
 */
public class DplResultSet {

	/**
	 * 参与Join的数据集合的名称
	 */
	private Set<String> resultNames;

	/**
	 * Join的结果集合
	 */
	private List<Map<String, Object>> resultSet;

	public DplResultSet() {
		resultNames = new HashSet<String>();
		resultSet = new ArrayList<Map<String, Object>>();
	}

	public Set<String> getResultNames() {
		return resultNames;
	}

	public void setResultNames(Set<String> resultNames) {
		this.resultNames = resultNames;
	}

	public List<Map<String, Object>> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<Map<String, Object>> resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * 返回一个<code>DplResultSet</code>的拷贝
	 * 
	 * @return
	 */
	public DplResultSet getCloneDplResultSet() {
		DplResultSet dplResultSet = new DplResultSet();
		dplResultSet.setResultNames(new HashSet<String>(resultNames));
		dplResultSet.setResultSet(new ArrayList<Map<String, Object>>(resultSet));
		return dplResultSet;
	}

}

// $Id: DplResultSet.java 15998 2010-07-12 06:36:59Z wei.zhang $