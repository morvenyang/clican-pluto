/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.dpl.parser.ParserObject;

/**
 * 子查询对象
 * 
 * @author wei.zhang
 * 
 */
public class SubDpl implements ParserObject {

	/**
	 * 子查询dpl和别名的映射
	 */
	private Map<String, String> subDplStrAliasMap = new HashMap<String, String>();

	/**
	 * 子查询别名和子查询结果集的映射
	 */
	private Map<String, Object> aliasResultMap = new HashMap<String, Object>();

	public Map<String, String> getSubDplStrAliasMap() {
		return subDplStrAliasMap;
	}

	public Map<String, Object> getAliasResultMap() {
		return aliasResultMap;
	}

	/**
	 * 添加一个子查询语句的别名和对应的结果集合
	 * 
	 * @param subDplStr
	 *            子查询的dpl
	 * @param alias
	 *            别名
	 * @param result
	 *            子查询的结果集
	 */
	public void addSubDpl(String subDplStr, String alias, Object result) {
		if (StringUtils.isEmpty(alias)) {
			alias = "dual." + UUID.randomUUID().toString().replaceAll("\\-", "");
		}
		aliasResultMap.put(alias, result);
		subDplStrAliasMap.put(subDplStr, alias);
	}

}

// $Id$