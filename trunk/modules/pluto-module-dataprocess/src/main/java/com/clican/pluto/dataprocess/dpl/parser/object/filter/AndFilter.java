/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.dpl.parser.bean.CombinedKey;
import com.clican.pluto.dataprocess.dpl.parser.bean.DplResultSet;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 与的条件，把2部分的数据求交集
 * 
 * @author wei.zhang
 * 
 */
public class AndFilter extends Filter {

	private final static Log log = LogFactory.getLog(AndFilter.class);

	private Filter filter1;

	private Filter filter2;

	private From from;

	public AndFilter(Filter filter1, Filter filter2, From from) {
		this.from = from;
		if (filter1.priority() >= filter2.priority()) {
			this.filter1 = filter1;
			this.filter2 = filter2;
		} else {
			this.filter1 = filter2;
			this.filter2 = filter1;
		}
	}

	@Override
	public String getExpr() {
		return filter1.getExpr() + " and " + filter2.getExpr();
	}

	private void dumpTrace(String expr, ProcessorContext context) {
		if (log.isTraceEnabled()) {
			String output = "filter{" + expr + "}[";
			for (String attr : context.getAttributeNames()) {
				if (context.getAttribute(attr) instanceof List) {
					List<?> list = (List<?>) context.getAttribute(attr);
					output += attr + "=" + list.size() + ",";
				} else if (context.getAttribute(attr) instanceof DplResultSet) {
					DplResultSet rs = (DplResultSet) context.getAttribute(attr);
					output += attr + "=" + rs.getResultSet().size() + ",";
				}
			}
			output += "]";
			log.trace(output);
		}
	}

	@Override
	public void filter(ProcessorContext context) throws DplParseException {
		ProcessorContext context1 = context.getCloneContext();
		ProcessorContext context2;
		filter1.filter(context1);
		dumpTrace(filter1.getExpr(), context1);
		//对于AndFilter应该共享context上下文，所以这里直接用context1来clone
		context2 = context1.getCloneContext();
		filter2.filter(context2);
		dumpTrace(filter2.getExpr(), context2);

		DplResultSet dplResultSet1 = context1.getAttribute(CompareFilter.DPL_RESULT_SET);
		DplResultSet dplResultSet2 = context2.getAttribute(CompareFilter.DPL_RESULT_SET);
		if (dplResultSet1 != null && dplResultSet2 != null) {
			//如果返回的dplResultSet1和dplResultSet2是同一个对象就表明filter2内的比较是在dplResultSet1的基础上的过滤
			if (dplResultSet1 == dplResultSet2) {
				DplResultSet dplResultSet = new DplResultSet();
				dplResultSet.setResultNames(dplResultSet1.getResultNames());
				dplResultSet.setResultSet(dplResultSet1.getResultSet());
				context.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet);
			} else {
				// 合并在一起存在的list名称
				Set<String> names = new HashSet<String>();
				// 共同存在的list名称
				Set<String> shareNames = new HashSet<String>();

				for (String name : dplResultSet1.getResultNames()) {
					names.add(name);
				}
				for (String name : dplResultSet2.getResultNames()) {
					if (names.contains(name)) {
						shareNames.add(name);
					} else {
						names.add(name);
					}
				}
				Set<Map<String, Object>> filterSet = new HashSet<Map<String, Object>>();
				List<Map<String, Object>> filterList = new ArrayList<Map<String, Object>>();
				DplResultSet dplResultSet = new DplResultSet();
				dplResultSet.setResultNames(names);
				//这种case请参见FilterParserTestCase#testParseFilter5
				//select list1.id as id from list1,list2,list3,list4,list5 where (list1.id=list2.id and list2.id=list3.id) and (list4.id=list5.id and list3.id=list4.id)
				if (shareNames.size() != 0) {
					// 首先通过共同存在的list然后以各个list的name和value作为CombinedKey得到m1和m2
					// order1和order2只是为了记住各条记录原始的顺序保证交集和顺序不变
					Map<Set<CombinedKey>, List<Map<String, Object>>> m1 = new HashMap<Set<CombinedKey>, List<Map<String, Object>>>();
					Map<Set<CombinedKey>, List<Map<String, Object>>> m2 = new HashMap<Set<CombinedKey>, List<Map<String, Object>>>();
					Map<Map<String, Object>, Integer> order1 = new HashMap<Map<String, Object>, Integer>();
					Map<Map<String, Object>, Integer> order2 = new HashMap<Map<String, Object>, Integer>();
					final Map<Map<String, Object>, Integer[]> order = new HashMap<Map<String, Object>, Integer[]>();
					//装配m1和order1
					for (int i = 0; i < dplResultSet1.getResultSet().size(); i++) {
						Map<String, Object> rs1 = dplResultSet1.getResultSet().get(i);
						order1.put(rs1, i);
						Set<CombinedKey> combinedKeySet = new HashSet<CombinedKey>();
						for (String name : shareNames) {
							Object obj = rs1.get(name);
							CombinedKey key = new CombinedKey();
							key.setName(name);
							key.setValue(obj);
							combinedKeySet.add(key);
						}
						if (!m1.containsKey(combinedKeySet)) {
							m1.put(combinedKeySet, new ArrayList<Map<String, Object>>());
						}
						m1.get(combinedKeySet).add(rs1);
					}
					//装配m2和order2
					for (int i = 0; i < dplResultSet2.getResultSet().size(); i++) {
						Map<String, Object> rs2 = dplResultSet2.getResultSet().get(i);
						order2.put(rs2, i);
						Set<CombinedKey> combinedKeySet = new HashSet<CombinedKey>();
						for (String name : shareNames) {
							Object obj = rs2.get(name);
							CombinedKey key = new CombinedKey();
							key.setName(name);
							key.setValue(obj);
							combinedKeySet.add(key);
						}
						if (!m2.containsKey(combinedKeySet)) {
							m2.put(combinedKeySet, new ArrayList<Map<String, Object>>());
						}
						m2.get(combinedKeySet).add(rs2);
					}

					for (Set<CombinedKey> key : m1.keySet()) {
						if (m2.containsKey(key)) {
							// 对于具有相同的CombinedKey的集合求合集
							List<Map<String, Object>> list1 = m1.get(key);
							List<Map<String, Object>> list2 = m2.get(key);
							for (Map<String, Object> row1 : list1) {
								for (Map<String, Object> row2 : list2) {
									Map<String, Object> row = new HashMap<String, Object>();
									row.putAll(row1);
									row.putAll(row2);
									if (!filterSet.contains(row)) {
										filterList.add(row);
										Integer[] i = new Integer[] { order1.get(row1), order2.get(row2) };
										//对Join后的结果集生成联合order
										order.put(row, i);
									}
								}
							}
						}
					}
					//对结果集合根据联合order来排序，维护好Join前后的数据顺序
					Collections.sort(filterList, new Comparator<Map<String, Object>>() {
						@Override
						public int compare(Map<String, Object> map1, Map<String, Object> map2) {
							Integer[] o1 = order.get(map1);
							Integer[] o2 = order.get(map2);
							if (o1[0].equals(o2[0])) {
								return o1[1] - o2[1];
							} else {
								return o1[0] - o2[0];
							}
						}
					});
				} else {
					// 如果不存在相同name的情况下的2个集合则只能求笛卡尔集
					for (Map<String, Object> row1 : dplResultSet1.getResultSet()) {
						for (Map<String, Object> row2 : dplResultSet2.getResultSet()) {
							Map<String, Object> row = new HashMap<String, Object>();
							row.putAll(row1);
							row.putAll(row2);
							if (!filterSet.contains(row)) {
								filterList.add(row);
							}
						}
					}
				}
				dplResultSet.setResultSet(filterList);
				context.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet);
			}
		} else if (dplResultSet1 != null && dplResultSet2 == null) {
			context.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet1);
		} else if (dplResultSet2 != null && dplResultSet1 == null) {
			context.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet2);
		}
		//对于普通的List求交集并维护好先后顺序
		for (String name : from.getVariableNames()) {
			if (name.equals(CompareFilter.DPL_RESULT_SET)) {
				continue;
			}
			List<Object> list1 = context1.getAttribute(name);
			Set<Object> set = new HashSet<Object>();
			final Map<Object, Integer> map = new HashMap<Object, Integer>();
			for (int i = 0; i < list1.size(); i++) {
				map.put(list1.get(i), i);
				set.add(list1.get(i));
			}
			List<Object> list2 = context2.getAttribute(name);
			set.retainAll(new HashSet<Object>(list2));

			List<Object> list = new ArrayList<Object>(set);
			Collections.sort(list, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					int order1 = map.get(o1);
					int order2 = map.get(o2);
					return order1 - order2;
				}
			});
			context.setAttribute(name, list);
		}
		if (log.isTraceEnabled()) {
			log.trace("finish filter[" + this.getExpr() + "]");
		}
	}

	@Override
	public int priority() {
		return filter1.priority() + filter2.priority();
	}

}

// $Id: AndFilter.java 16089 2010-07-14 01:15:55Z wei.zhang $