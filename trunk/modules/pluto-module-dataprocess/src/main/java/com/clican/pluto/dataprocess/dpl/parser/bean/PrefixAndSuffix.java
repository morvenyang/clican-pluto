/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.common.util.PropertyUtilS;
import com.clican.pluto.common.util.TypeUtils;
import com.clican.pluto.dataprocess.dpl.function.FunctionCallback;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.impl.FromParser;
import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 前缀和后缀的解析
 * 
 * 比如list1.person.name中list1是前缀person.name是后缀
 * 
 * 比如person则前缀为空后缀为person
 * 
 * @author clican
 * 
 */
public class PrefixAndSuffix {

	private final static Log log = LogFactory.getLog(PrefixAndSuffix.class);

	private final static ThreadLocal<List<ProcessorContext>> localContext = new ThreadLocal<List<ProcessorContext>>();

	/**
	 * 前缀只可能是dual和from中枚举的列表
	 */
	private String prefix;

	/**
	 * 后缀只可能是常量,from中枚举的列表的属性和dual中的常量
	 */
	private String suffix;

	private String expr;

	private Function function;

	private boolean supportInMultiFunctionWithoutGroupBy;

	public PrefixAndSuffix(Function function) {
		this.function = function;
	}

	public List<String> getFromParams() {
		if (function != null) {
			return function.getFromParams();
		} else {
			List<String> fromParams = new ArrayList<String>();
			if (StringUtils.isNotEmpty(prefix)
					&& !prefix.equals(FromParser.CONSTANTS_KEY)) {
				fromParams.add(prefix);
			}
			return fromParams;
		}
	}

	public PrefixAndSuffix(String expr) throws PrefixAndSuffixException {
		this.expr = expr;
		if (expr.startsWith("'") && expr.endsWith("'")) {
			suffix = expr;
		} else if (NumberUtils.isNumber(expr)) {
			suffix = expr;
		} else if (expr.startsWith(FromParser.CONSTANTS_KEY + ".")) {
			prefix = FromParser.CONSTANTS_KEY;
			suffix = expr.substring(expr.indexOf(".") + 1);
		} else {
			supportInMultiFunctionWithoutGroupBy = false;
			if (expr.contains(".")) {
				prefix = expr.substring(0, expr.indexOf("."));
				suffix = expr.substring(expr.indexOf(".") + 1);
			} else {
				prefix = expr;
			}
		}
	}

	public static void setLocalContext(ProcessorContext context) {
		List<ProcessorContext> contextList = localContext.get();
		if (contextList == null) {
			contextList = new LinkedList<ProcessorContext>();
			localContext.set(contextList);

		}
		contextList.add(context);
	}

	public static void releaseLocalContext() {
		List<ProcessorContext> contextList = localContext.get();
		contextList.remove(contextList.size() - 1);
		if (contextList.size() == 0) {
			localContext.remove();
		}
	}

	private ProcessorContext getProcessorContext() {
		List<ProcessorContext> contextList = localContext.get();
		return contextList.get(contextList.size() - 1);
	}

	public void isSupportInMultiFunctionWithoutGroupBy()
			throws PrefixAndSuffixException {
		if (!supportInMultiFunctionWithoutGroupBy) {
			throw new PrefixAndSuffixException("在有多行处理函数并且没有分组的情况下,不支持普通列的查询");
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getConstantsValue() throws CalculationException,
			PrefixAndSuffixException {
		return (T) getValue(new HashMap<String, Object>());
	}

	/**
	 * 从<code>ProcessorContext</code>中获得一个常量。
	 * <p>
	 * 并且把该常量在允许的情况下转换为clazz类型。
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws CalculationException
	 * @throws PrefixAndSuffixException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getConstantsValue(Class<?> clazz) throws CalculationException,
			PrefixAndSuffixException {
		Object obj = getConstantsValue();
		if (obj == null) {
			return null;
		}
		if (obj.getClass().equals(clazz)) {
			return (T) obj;
		} else {
			if (Number.class.isAssignableFrom(clazz)) {
				if (obj instanceof Number) {
					return (T) TypeUtils.numberToNumber((Number) obj, clazz);
				} else {
					if (StringUtils.isEmpty(obj.toString())) {
						return null;
					} else {
						return (T) TypeUtils.stringToNumber(obj.toString(),
								clazz);
					}
				}
			} else if (clazz.equals(String.class)) {
				if (obj instanceof Number) {
					return (T) (((Number) obj).intValue() + "");
				} else {
					return (T) obj.toString();
				}
			} else if (clazz.equals(Date.class)) {
				return (T) obj;
			} else {
				return (T) obj;
			}
		}
	}

	public Function getFunction() {
		return function;
	}

	/**
	 * 根据单行的row获得该PrefixAndSuffix描述的对象的值
	 * 
	 * @param <T>
	 * @param row
	 *            单行记录
	 * @return row单行记录中根据PrefixAndSuffix描述对应的值
	 * @throws CalculationException
	 * @throws PrefixAndSuffixException
	 */
	@SuppressWarnings( { "unchecked" })
	public <T> T getValue(Map<String, Object> row) throws CalculationException,
			PrefixAndSuffixException {
		if (row == null) {
			return null;
		}
		if (function != null) {
			if (row.containsKey(function.getId())) {
				Object obj = row.get(function.getId());
				if (obj instanceof FunctionCallback) {
					return (T) (((FunctionCallback) obj).getValue());
				} else {
					return (T) obj;
				}
			} else {
				if (function instanceof SingleRowFunction) {
					return (T) ((SingleRowFunction) function).recurseCalculate(
							null, row);
				} else {
					throw new PrefixAndSuffixException(function.getExpr()
							+ "还未被运算无法返回运算结果");
				}
			}
		} else {
			// 没有前缀表示不带'.'
			if (StringUtils.isEmpty(prefix)) {
				// 在prefix为空的情况下suffix只可能是常量
				if (suffix.startsWith("'") && suffix.endsWith("'")) {
					return (T) suffix.substring(1, suffix.length() - 1);
				} else if (row.containsKey(suffix)) {
					return (T) row.get(suffix);
				} else if (NumberUtils.isNumber(suffix)) {
					// 不停的尝试各种Number类型
					if (suffix.contains(".")) {
						return (T) Double.valueOf(suffix);
					} else {
						return (T) Integer.valueOf(suffix);
					}
				} else if (suffix.equalsIgnoreCase("null")) {
					return null;
				} else {
					// 在无法识别的情况下默认就返回suffix本身
					return (T) suffix;
				}
			} else {
				if (prefix.equals(FromParser.CONSTANTS_KEY)) {
					if (StringUtils.isNotEmpty(suffix)) {
						T result = (T) getProcessorContext().getAttribute(
								suffix);
						if (result == null) {
							if (suffix.contains(".")) {
								String name = suffix.substring(0, suffix
										.indexOf("."));
								if (getProcessorContext().contains(name)) {
									Object obj = getProcessorContext()
											.getAttribute(name);
									if (obj == null) {
										log
												.trace("该常量在ProcessorContext中为空,因此获取其属性也为空");
									} else {
										try {
											obj = PropertyUtilS
													.getNestedProperty(
															obj,
															suffix
																	.substring(suffix
																			.indexOf(".") + 1));
										} catch (Exception e) {
											throw new PrefixAndSuffixException(
													"获得内联属性出错", e);
										}
									}
									return (T) obj;
								} else {
									return null;
								}
							} else {
								if (getProcessorContext().contains(suffix)) {
									return null;
								} else {
									return null;
								}
							}

						} else {
							return result;
						}
					} else {
						throw new PrefixAndSuffixException(
								"对于从dual常量中取得数据的情况下必须要有suffix,如果没有suffix的话可能存在解析错误");
					}
				} else {
					Object obj = row.get(prefix);
					if (obj == null) {
						obj = row.get(prefix + "." + suffix);
						if (obj == null) {
							return null;
						} else {
							return (T) obj;

						}
					} else {
						if (StringUtils.isEmpty(suffix)) {
							return (T) obj;
						} else {
							if (suffix.equals("*")) {
								return (T) obj;
							} else {
								try {
									obj = PropertyUtilS.getNestedProperty(obj,
											suffix);
								} catch (Exception e) {
									throw new PrefixAndSuffixException(
											"获得内联属性出错", e);
								}
								return (T) obj;
							}
						}
					}
				}
			}
		}

	}

	public <T> List<T> getValues(List<Map<String, Object>> rowSet,
			ProcessorContext context) throws CalculationException,
			PrefixAndSuffixException {
		List<T> values = new ArrayList<T>(rowSet.size());
		for (Map<String, Object> row : rowSet) {
			T o = this.<T> getValue(row);
			if (o != null) {
				values.add(o);
			}
		}
		return values;
	}

	@Override
	public String toString() {
		return expr;
	}

}

// $Id: PrefixAndSuffix.java 16219 2010-07-16 02:25:35Z wei.zhang $