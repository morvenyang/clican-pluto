/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.dpl.parser.DplParser;
import com.clican.pluto.dataprocess.dpl.parser.eunmeration.CompareType;
import com.clican.pluto.dataprocess.dpl.parser.object.SubDpl;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplException;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 解析子查询语句
 * 
 * @author clican
 * 
 */
public class SubDplParser implements DplParser {

	private final static Log log = LogFactory.getLog(SubDplParser.class);

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public SubDpl parse(String dpl, ProcessorContext context) throws DplParseException {
		log.debug("parse sub dpl[" + dpl + "]");
		SubDpl subDpl = new SubDpl();
		dpl = dpl.trim();
		if (dpl.startsWith(SelectParser.START_KEYWORD)) {
			dpl = dpl.replaceFirst(SelectParser.START_KEYWORD, "");
		}
		boolean findSelect = false;
		int leftIndex = -1;
		int rightIndex = -1;
		int left = 0;
		int right = 0;
		int index = 0;
		CompareType lastCompareType = null;
		try {
			while (index < dpl.length()) {
				String token = dpl.substring(index, index + 1);
				String subStr = dpl.substring(index);
				if (!findSelect) {
					for (CompareType ct : CompareType.values()) {
						if (subStr.startsWith(ct.getOperation())) {
							lastCompareType = ct;
						}
					}
				}
				if (token.equals("(")) {
					left++;
					if (leftIndex == -1 || !findSelect) {
						leftIndex = index;
					}
				}
				if (token.equals(")")) {
					right++;
				}
				if (findSelect && left == right && left > 0) {
					rightIndex = index + 1;
					String subDplStr = dpl.substring(leftIndex, rightIndex);

					List<Map<String, Object>> result = dplStatement.execute(subDplStr.substring(1, subDplStr.length() - 1), context);
					Object lastResult = result;
					String alias = null;
					String tailDplStr = dpl.substring(rightIndex);
					// 判断子查询语句是否带有as别名，如果有的话则该子查询是属于from中的子查询
					if (tailDplStr.startsWith(SelectParser.AS_TOKEN)) {
						int i1 = tailDplStr.indexOf(",");
						int i2 = tailDplStr.indexOf(FilterParser.START_KEYWORD);
						int i = -1;
						if (i1 > 0 && i2 > 0) {
							i = i1 < i2 ? i1 : i2;
						} else if (i1 > 0) {
							i = i1;
						} else if (i2 > 0) {
							i = i2;
						} else {
							i = tailDplStr.length();
						}
						alias = tailDplStr.substring(SelectParser.AS_TOKEN.length(), i).trim();

						subDplStr = dpl.substring(leftIndex, rightIndex + i);
					} else {
						// 没有别名的情况下说明该子查询是在where条件语句中的子查询
						if (lastCompareType != null) {
							if (lastCompareType != CompareType.IN && lastCompareType != CompareType.NOT_IN) {
								// 如果子查询前的操作不是in和not in则只允许子查询返回一条结果
								if (result.size() == 1) {
									Map<String, Object> row = result.get(0);
									if (row.size() == 1) {
										lastResult = new ArrayList<Object>(row.values()).get(0);
									} else {
										throw new DplException("在where条件子查询[" + subDplStr + "]返回的结果集合只能是单列");
									}
								} else {
									throw new DplException("子查询[" + subDplStr + "]返回的结果集合不是一条无法和该操作" + lastCompareType.getOperation() + "做比较");
								}
							} else {
								// 如果子查询前的操作是in或not in则子查询返回的结果必须是单列的
								List<Object> temp = new ArrayList<Object>();
								for (Map<String, Object> row : result) {
									if (row.size() == 1) {
										temp.add(new ArrayList<Object>(row.values()).get(0));
									} else {
										throw new DplException("在where条件子查询[" + subDplStr + "]返回的结果集合只能是单列");
									}
								}
								lastResult = temp;
							}
						} else {
							throw new DplException("子查询[" + subDplStr + "]解析错误");
						}
					}
					subDpl.addSubDpl(subDplStr, alias, lastResult);
					findSelect = false;
					left = 0;
					right = 0;
					leftIndex = -1;
					rightIndex = -1;
					lastCompareType = null;
				}
				if (subStr.startsWith(SelectParser.START_KEYWORD + " ")) {
					findSelect = true;
				}
				index++;
			}
		} catch (Exception e) {
			log.error("", e);
			throw new DplParseException(e);
		}

		return subDpl;
	}
}

// $Id$