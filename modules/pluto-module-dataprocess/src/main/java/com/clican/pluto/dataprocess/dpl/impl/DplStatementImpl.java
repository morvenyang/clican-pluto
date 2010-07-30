/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.common.util.TypeUtils;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.dpl.comparator.GroupConditionComparator;
import com.clican.pluto.dataprocess.dpl.comparator.OrderByComparator;
import com.clican.pluto.dataprocess.dpl.function.MultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.bean.Column;
import com.clican.pluto.dataprocess.dpl.parser.bean.DplResultSet;
import com.clican.pluto.dataprocess.dpl.parser.bean.Group;
import com.clican.pluto.dataprocess.dpl.parser.bean.GroupCondition;
import com.clican.pluto.dataprocess.dpl.parser.impl.FilterParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.FromParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.GroupByParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.OrderByParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.PagingParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.SelectParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.SubDplParser;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.dpl.parser.object.GroupBy;
import com.clican.pluto.dataprocess.dpl.parser.object.OrderBy;
import com.clican.pluto.dataprocess.dpl.parser.object.Pagination;
import com.clican.pluto.dataprocess.dpl.parser.object.Select;
import com.clican.pluto.dataprocess.dpl.parser.object.SubDpl;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.CompareFilter;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.Filter;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.exception.DplException;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 在内存中实现了<code>DplStatement</code>
 * <p>
 * 目前也是<code>DplStatement</code>的唯一的一个实现方式。
 * 
 * 通过解析DPL来获得响应的Select, From, Filter, Order By 和 Group By。
 * 
 * <ul>
 * <li>第一步，格式化换行空格等</li>
 * <li>第二步，处理子查询</li>
 * <li>第三步，解析各个查询部分</li>
 * <li>第四部，根据Filter条件来过滤数据集和</li>
 * <li>第五步，根据Group By条件对数据集合进行分组</li>
 * <li>第六步，如果存在分组条件则根据分组后的数据返回Select结果集合；如果不存在分组条件则根据Filter的结果集合返回Select结果集合。
 * 其中Select中的function计算也是根据对应的结果集合来计算的。</li>
 * <li>第七步，根据第四步得到的结果集合进行排序。为了实现的简单期间目前排序用到的名称就是select结果的as的名称</li>
 * <li>第八步，对返回结果集和进行分页</li>
 * </ul>
 * 
 * <p>
 * 默认返回的结果集合是一个List集合。其中的数据用Map的形式来描述每个列的数据。 如果外部系统调用了带有Class类型参数的接口的话。
 * <code>DplStatementImpl</code>就会把Map对象转换为对应的Class然后再返回给外部系统。
 * <p/>
 * 
 * @author wei.zhang
 * 
 */
public class DplStatementImpl implements DplStatement {

	private final static Log log = LogFactory.getLog(DplStatementImpl.class);

	public final static String CONTEXT_NAME = "dplStatement";

	/**
	 * <code>FilterParser</code>实例
	 */
	private FilterParser filterParser;
	/**
	 * <code>GroupByParser</code>实例
	 */
	private GroupByParser groupByParser;
	/**
	 * <code>OrderByParser</code>实例
	 */
	private OrderByParser orderByParser;
	/**
	 * <code>SelectParser</code>实例
	 */
	private SelectParser selectParser;
	/**
	 * <code>FromParser</code>实例
	 */
	private FromParser fromParser;

	private PagingParser pagingParser;

	private SubDplParser subDplParser;

	public void setFilterParser(FilterParser filterParser) {
		this.filterParser = filterParser;
	}

	public void setGroupByParser(GroupByParser groupByParser) {
		this.groupByParser = groupByParser;
	}

	public void setOrderByParser(OrderByParser orderByParser) {
		this.orderByParser = orderByParser;
	}

	public void setSelectParser(SelectParser selectParser) {
		this.selectParser = selectParser;
	}

	public void setFromParser(FromParser fromParser) {
		this.fromParser = fromParser;
	}

	public void setPagingParser(PagingParser pagingParser) {
		this.pagingParser = pagingParser;
	}

	public void setSubDplParser(SubDplParser subDplParser) {
		this.subDplParser = subDplParser;
	}

	/**
	 * @see DplStatement#execute(String, ProcessorContext)
	 */
	
	public List<Map<String, Object>> execute(String dpl, ProcessorContext context) throws DplException {
		dpl = trimDpl(dpl);
		if (dpl.trim().length() == 0) {
			log.warn("dpl statement empty, simple return null");
			return null;
		}
		ProcessorContext clone = context.getCloneContext();
		// 解析dpl的各个组成部分
		Map<String, Object> parseContext = new HashMap<String, Object>();
		parseContext.put(CONTEXT_NAME, this);
		SubDpl subDpl = subDplParser.parse(dpl, clone);
		for (String subDplStr : subDpl.getSubDplStrAliasMap().keySet()) {
			String alias = subDpl.getSubDplStrAliasMap().get(subDplStr);
			Object result = subDpl.getAliasResultMap().get(alias);
			dpl = StringUtils.replaceOnce(dpl, subDplStr, alias);
			if (alias.startsWith("dual")) {
				clone.setAttribute(alias.substring(5), result);
			} else {
				clone.setAttribute(alias, result);
			}
		}
		From from = fromParser.parse(dpl, clone);
		if (from.getVariableNames().size() == 0) {
			throw new DplParseException("From关键字解析错误");
		}
		Filter filter = filterParser.parse(dpl, clone);
		GroupBy groupBy = groupByParser.parse(dpl, clone);
		OrderBy orderBy = orderByParser.parse(dpl, clone);
		Select select = selectParser.parse(dpl, clone);
		Pagination pagination = pagingParser.parse(dpl, clone);

		// 过滤数据集合并且返回结果的数据集合
		DplResultSet dplResultSet = null;
		if (filter != null) {
			filter.filter(clone);
			dplResultSet = clone.getAttribute(CompareFilter.DPL_RESULT_SET);
		}
		if (dplResultSet == null) {
			dplResultSet = new DplResultSet();
			for (String name : from.getVariableNames()) {
				dplResultSet.getResultNames().add(name);
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
				List<Object> list = clone.getAttribute(name);
				if (list.size() == 0) {
					dplResultSet = new DplResultSet();
					break;
				}
				if (dplResultSet.getResultSet().size() == 0) {
					for (Object obj : list) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(name, obj);
						result.add(map);
					}
					dplResultSet.setResultSet(result);
				} else {
					for (Object obj : list) {
						for (Map<String, Object> row : new ArrayList<Map<String, Object>>(dplResultSet.getResultSet())) {
							Map<String, Object> map = new HashMap<String, Object>(row);
							map.put(name, obj);
							result.add(map);
						}
					}
					dplResultSet.setResultSet(result);
				}
			}
		}
		// 获得Join后的数据集合

		// 获得被过滤过的原始数据集合
		Map<String, Set<Object>> data = new HashMap<String, Set<Object>>();
		for (String name : from.getVariableNames()) {
			if (!name.equals(CompareFilter.DPL_RESULT_SET)) {
				List<Object> list = clone.getAttribute(name);
				data.put(name, new HashSet<Object>(list));
			}
		}
		// 准备结果数据集合
		List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();

		// 通过被过滤过的原始数据集合再过滤Join后的数据集合
		for (Map<String, Object> result : dplResultSet.getResultSet()) {
			boolean valid = true;
			for (String key : result.keySet()) {
				if (result.get(key) != null && !data.get(key).contains(result.get(key))) {
					valid = false;
				}
			}
			if (valid) {
				rs.add(result);
			}
		}
		// 准备根据分组条件得到的数据集合
		Map<List<GroupCondition>, List<Map<String, Object>>> groupByRs = new HashMap<List<GroupCondition>, List<Map<String, Object>>>();

		// 处理分组，把各个数据集合根据分组条件来分组
		if (groupBy != null) {
			try {
				for (Map<String, Object> result : rs) {
					List<GroupCondition> gcSet = new ArrayList<GroupCondition>();
					for (int i = 0; i < groupBy.getGroups().size(); i++) {
						Group group = groupBy.getGroups().get(i);
						Object groupValue = group.getValue(result);
						GroupCondition gc = new GroupCondition();
						gc.setPosition(i);
						gc.setGroupName(group.getExpr());
						gc.setGroupValue(groupValue);
						gcSet.add(gc);
					}
					if (!groupByRs.containsKey(gcSet)) {
						groupByRs.put(gcSet, new ArrayList<Map<String, Object>>());
					}
					groupByRs.get(gcSet).add(result);
				}
			} catch (Exception e) {
				throw new DplParseException(e);
			}
		}
		// 准备新的一个Select和Function计算后的数据集合
		List<Map<String, Object>> newRs = new ArrayList<Map<String, Object>>();

		try {
			if (groupBy != null) {
				List<List<GroupCondition>> temp = new ArrayList<List<GroupCondition>>(groupByRs.keySet());
				// 首先把各个分组条件做排序
				Collections.sort(temp, new GroupConditionComparator());
				// 在分组条件下根据分组后的数据集合来获得Select后的数据集合
				for (List<GroupCondition> gcSet : temp) {
					Map<String, Object> groupMap = new HashMap<String, Object>();
					for (GroupCondition gc : gcSet) {
						groupMap.put(gc.getGroupName(), gc.getGroupValue());
					}
					Map<String, Object> row = new HashMap<String, Object>();

					for (Object tempColumn : select.getColumns()) {
						if (tempColumn instanceof Column) {
							Column column = (Column) tempColumn;
							String key = column.getColumnName();
							Object value = column.getPrefixAndSuffix().getValue(groupMap);
							row.put(key, value);
							groupMap.put(key, value);
						} else if (tempColumn instanceof SingleRowFunction) {
							SingleRowFunction cal = (SingleRowFunction) tempColumn;
							String key = cal.getColumnName();
							if (groupMap.containsKey(cal.getExpr())) {
								Object value = cal.getExpr();
								row.put(key, value);
								groupMap.put(key, value);
							} else {
								Object value = cal.recurseCalculate(groupByRs.get(gcSet), groupMap);
								row.put(key, value);
								groupMap.put(key, value);
							}
						} else if (tempColumn instanceof MultiRowFunction) {
							MultiRowFunction cal = (MultiRowFunction) tempColumn;
							String key = cal.getColumnName();
							Object value = cal.recurseCalculate(groupByRs.get(gcSet));
							row.put(key, value);
							groupMap.put(key, value);
						} else {
							throw new DplException("Select对象不能被识别");
						}
					}
					newRs.add(row);
				}
			} else {
				// 不在分组条件下根据Join后和过滤后的集合来获得Select后的数据集合
				if (select.containMultiRowCalculation()) {
					// 在有多行处理函数并且没有分组的情况下
					Map<String, Object> map = new HashMap<String, Object>();
					Map<String, Object> row = new HashMap<String, Object>();
					map.putAll(context.getMap());
					for (Object tempColumn : select.getColumns()) {
						if (tempColumn instanceof Column) {
							Column column = (Column) tempColumn;
							String key = column.getColumnName();
							Object value = column.getPrefixAndSuffix().getValue(map);
							if (value == null) {
								column.getPrefixAndSuffix().isSupportInMultiFunctionWithoutGroupBy();
							}
							map.put(key, value);
							row.put(key, value);
						} else if (tempColumn instanceof SingleRowFunction) {
							SingleRowFunction cal = (SingleRowFunction) tempColumn;
							String key = cal.getColumnName();
							Object value = cal.recurseCalculate(rs, map);
							map.put(key, value);
							row.put(key, value);
						} else if (tempColumn instanceof MultiRowFunction) {
							MultiRowFunction cal = (MultiRowFunction) tempColumn;
							String key = cal.getColumnName();
							Object value = cal.recurseCalculate(rs);
							map.put(key, value);
							row.put(key, value);
						} else {
							throw new DplException("Select对象不能被识别");
						}
					}
					newRs.add(row);
				} else {
					for (Map<String, Object> map : rs) {
						Map<String, Object> row = new HashMap<String, Object>();
						for (Object tempColumn : select.getColumns()) {
							if (tempColumn instanceof Column) {
								Column column = (Column) tempColumn;
								String key = column.getColumnName();
								Object value = column.getPrefixAndSuffix().getValue(map);
								map.put(key, value);
								row.put(key, value);
							} else if (tempColumn instanceof SingleRowFunction) {
								SingleRowFunction cal = (SingleRowFunction) tempColumn;
								String key = cal.getColumnName();
								Object value = cal.recurseCalculate(rs, map);
								map.put(key, value);
								row.put(key, value);
							} else if (tempColumn instanceof MultiRowFunction) {
								throw new DplException("不包括多行处理函数，不需要被执行");
							} else {
								throw new DplException("Select对象不能被识别");
							}
						}
						newRs.add(row);
					}
				}
			}

		} catch (Exception e) {
			throw new DplException(e);
		}
		// 准备排序后的数据集合
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(newRs);
		if (orderBy != null) {
			Collections.sort(list, new OrderByComparator(orderBy));
		}
		// 返回最终的数据集合
		if (pagination != null) {
			if (pagination.isReverse()) {
				if (pagination.getLimit() == null) {
					if (list.size() - pagination.getOffset() > 0) {
						return list.subList(0, list.size() - pagination.getOffset());
					} else {
						return new ArrayList<Map<String, Object>>();
					}
				} else {
					if (list.size() - pagination.getOffset() > 0) {
						if (list.size() - pagination.getOffset() - pagination.getLimit() >= 0) {
							return list.subList(list.size() - pagination.getOffset() - pagination.getLimit(), list.size() - pagination.getOffset());
						} else {
							return list.subList(0, list.size() - pagination.getOffset());
						}
					} else {
						return new ArrayList<Map<String, Object>>();
					}
				}
			} else {
				if (pagination.getLimit() == null) {
					if (list.size() - pagination.getOffset() > 0) {
						return list.subList(pagination.getOffset(), list.size());
					} else {
						return new ArrayList<Map<String, Object>>();
					}
				} else {
					if (list.size() - pagination.getOffset() > 0) {
						if (list.size() - pagination.getOffset() - pagination.getLimit() >= 0) {
							return list.subList(pagination.getOffset(), pagination.getOffset() + pagination.getLimit());
						} else {
							return list.subList(pagination.getOffset(), list.size());
						}
					} else {
						return new ArrayList<Map<String, Object>>();
					}
				}

			}
		} else {
			return list;
		}

	}

	/**
	 * 过滤注释
	 * 
	 * @param dpl
	 * @return
	 */
	private String trimDpl(String dpl) {
		String[] dplLines = dpl.split("\n");
		StringBuffer buf = new StringBuffer(dpl.length());

		for (String dplLine : dplLines) {
			if (dplLine.trim().startsWith("--")) {
				continue;
			} else {
				// 每行前后加个空格
				dplLine = " " + dplLine.trim() + " ";
			}
			buf.append(dplLine);
		}

		dpl = buf.toString();
		// 把所有的tab换成空格
		dpl = dpl.replaceAll("\t", " ");
		return dpl;
	}

	/**
	 * @see DplStatement#execute(String, ProcessorContext, Class)
	 */
	@SuppressWarnings("unchecked")
	
	public <T> List<T> execute(String dpl, ProcessorContext context, Class<T> clazz) throws DplException {
		List<Map<String, Object>> list = this.execute(dpl, context);
		if (clazz == null) {
			return (List<T>) list;
		}
		List<T> result = new ArrayList<T>();
		for (Map<String, Object> row : list) {
			T t = null;
			// 如果返回的结果集合只有一列，那么就返回该对象本身
			if (row.size() == 1) {
				t = (T) row.entrySet().iterator().next().getValue();
			} else {
				// 如果返回的结果集合有多列，那么根据参数给定的clazz类型来反射得到需要返回的结果类型
				try {
					t = clazz.newInstance();
				} catch (Exception e) {
					throw new DplException(e);
				}
				Method[] methods = clazz.getMethods();
				Map<String, Method> methodMap = new HashMap<String, Method>();
				for (Method method : methods) {
					methodMap.put(method.getName(), method);
					methodMap.put(method.getName().toLowerCase(), method);
				}
				for (String name : row.keySet()) {
					try {
						if (row.get(name) != null) {
							String setMethodName = com.clican.pluto.common.util.StringUtils.getSetMethodName(name);
							Object value = row.get(name);
							if (methodMap.containsKey(setMethodName) || methodMap.containsKey(setMethodName.toLowerCase())) {
								Class type = methodMap.get(setMethodName.toLowerCase()).getParameterTypes()[0];
								if (!value.getClass().equals(type)) {
									if (value instanceof Number) {
										value = TypeUtils.numberToNumber((Number) value, type);
									} else if (value instanceof String) {
										value = TypeUtils.stringToNumber((String) value, type);
									}
								}
								if (methodMap.containsKey(setMethodName)) {
									methodMap.get(setMethodName).invoke(t, new Object[] { value });
								} else {
									methodMap.get(setMethodName.toLowerCase()).invoke(t, new Object[] { value });
								}
							} else {
								if (log.isTraceEnabled()) {
									log.trace("There is no this property [" + name + "]");
								}
							}
						}
					} catch (Exception e) {
						if (log.isTraceEnabled()) {
							log.trace("There is no this property [" + name + "]");
						}
					}
				}
			}

			result.add(t);
		}
		return result;
	}

	
	public <T> List<T> execute(String dpl, Map<String, Object> context, Class<T> clazz) throws DplException {
		ProcessorContext ctx = new ProcessorContextImpl();
		for (String key : context.keySet()) {
			ctx.setAttribute(key, context.get(key));
		}
		return this.execute(dpl, ctx, clazz);
	}

	
	public List<Map<String, Object>> execute(String dpl, Map<String, Object> context) throws DplException {
		ProcessorContext ctx = new ProcessorContextImpl();
		for (String key : context.keySet()) {
			ctx.setAttribute(key, context.get(key));
		}
		return this.execute(dpl, ctx);
	}

}

// $Id: DplStatementImpl.java 16208 2010-07-16 00:57:34Z wei.zhang $