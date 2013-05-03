/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.dpl.parser.bean.DplResultSet;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 或的条件，把2部分的数据求并集
 * 
 * @author clican
 * 
 */
public class OrFilter extends Filter {

	private final static Log log = LogFactory.getLog(OrFilter.class);

	private Filter filter1;

	private Filter filter2;

	public OrFilter(Filter filter1, Filter filter2) {
		if (filter1.priority() > filter2.priority()) {
			this.filter1 = filter1;
			this.filter2 = filter2;
		} else {
			this.filter1 = filter2;
			this.filter2 = filter1;
		}
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

	public void filter(ProcessorContext context) throws DplParseException {
		ProcessorContext context1 = context.getCloneContext();
		DplResultSet dplResultSet1 = context1.getAttribute(CompareFilter.DPL_RESULT_SET);
		if (dplResultSet1 != null) {
			context1.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet1.getCloneDplResultSet());
		}
		ProcessorContext context2 = context.getCloneContext();
		DplResultSet dplResultSet2 = context2.getAttribute(CompareFilter.DPL_RESULT_SET);
		if (dplResultSet2 != null) {
			context2.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet2.getCloneDplResultSet());
		}
		filter1.filter(context1);
		dumpTrace(filter1.getExpr(), context1);
		filter2.filter(context2);
		dumpTrace(filter2.getExpr(), context2);
		dplResultSet1 = context1.getAttribute(CompareFilter.DPL_RESULT_SET);
		dplResultSet2 = context2.getAttribute(CompareFilter.DPL_RESULT_SET);
		if (dplResultSet1 != null && dplResultSet2 != null) {
			Set<String> names = new HashSet<String>();
			for (String name : dplResultSet1.getResultNames()) {
				names.add(name);
			}
			for (String name : dplResultSet2.getResultNames()) {
				names.add(name);
			}
			DplResultSet dplResultSet = new DplResultSet();
			dplResultSet.setResultNames(names);
			for (Map<String, Object> rs1 : dplResultSet1.getResultSet()) {
				if (dplResultSet2.getResultSet().size() == 0) {
					Map<String, Object> result = new HashMap<String, Object>(rs1);
					dplResultSet.getResultSet().add(result);
				} else {
					for (Map<String, Object> rs2 : dplResultSet2.getResultSet()) {
						Map<String, Object> result = new HashMap<String, Object>(rs1);
						for (String key : rs2.keySet()) {
							if (!result.containsKey(key)) {
								result.put(key, rs2.get(key));
							}
						}
						dplResultSet.getResultSet().add(result);
					}
				}
			}

			for (Map<String, Object> rs2 : dplResultSet2.getResultSet()) {
				if (dplResultSet1.getResultSet().size() == 0) {
					Map<String, Object> result = new HashMap<String, Object>(rs2);
					dplResultSet.getResultSet().add(result);
				} else {
					for (Map<String, Object> rs1 : dplResultSet1.getResultSet()) {
						Map<String, Object> result = new HashMap<String, Object>(rs2);
						for (String key : rs1.keySet()) {
							if (!result.containsKey(key)) {
								result.put(key, rs1.get(key));
							}
						}
						dplResultSet.getResultSet().add(result);
					}
				}
			}
			context.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet);
		} else if (dplResultSet1 != null && dplResultSet2 == null) {
			context.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet1);
		} else if (dplResultSet2 != null && dplResultSet1 == null) {
			context.setAttribute(CompareFilter.DPL_RESULT_SET, dplResultSet2);
		}

		for (String name : context.getAttributeNames()) {
			if (name.equals(CompareFilter.DPL_RESULT_SET)) {
				continue;
			}
			if (!(context1.getAttribute(name) instanceof List)) {
				continue;
			}
			List<Object> list1 = context1.getAttribute(name);
			List<Object> list2 = context2.getAttribute(name);
			if (list1 == list2) {
				context.setAttribute(name, list1);
				continue;
			}
			list2.removeAll(list1);
			list1.addAll(list2);
			context.setAttribute(name, list1);
		}
	}

	public String getExpr() {
		return filter1.getExpr() + " or " + filter2.getExpr();
	}

	public int priority() {
		return -(filter1.priority() + filter2.priority());
	}
}

// $Id: OrFilter.java 16146 2010-07-15 02:48:27Z wei.zhang $