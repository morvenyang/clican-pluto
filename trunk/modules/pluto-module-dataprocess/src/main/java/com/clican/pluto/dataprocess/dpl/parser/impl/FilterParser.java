/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.DplParser;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.eunmeration.CompareType;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.AndFilter;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.CompareFilter;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.Filter;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.OrFilter;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 对where关键字进行解析的解吸器
 * 
 * @author wei.zhang
 * 
 */
public class FilterParser implements DplParser {

	public final static Log log = LogFactory.getLog(FilterParser.class);
	/**
	 * 解析的开始位置
	 */
	public final static String START_KEYWORD = "where";

	/**
	 * 解析的结束位置
	 */
	private final static Set<String> END_KEYWORD = new HashSet<String>();

	private final static String AND_TOKEN = " and ";

	private final static String OR_TOKEN = " or ";

	static {
		END_KEYWORD.add(OrderByParser.START_KEYWORD);
		END_KEYWORD.add(GroupByParser.START_KEYWORD);
	}

	private FunctionParser functionParser;

	public void setFunctionParser(FunctionParser functionParser) {
		this.functionParser = functionParser;
	}

	/**
	 * 递归的解析And Or条件
	 * 
	 * @param s
	 *            where后的描述条件的一部分
	 * @param from
	 *            From对象
	 * @return
	 */
	private Filter parseFilter(String condition, ProcessorContext context, Map<String, Object> parseContext) throws DplParseException {
		// 如果最外层带有括号则直接去掉这对括号
		condition = condition.trim();
		if (condition.startsWith("(") && condition.endsWith(")")) {
			int index = 0;
			int left = 0;
			int right = 0;
			boolean remove = true;
			while (index < condition.length()) {
				String token = condition.substring(index, index + 1);
				if (token.equals("(")) {
					left++;
				} else if (token.equals(")")) {
					right++;
				}
				if (left == right && index != condition.length() - 1) {
					remove = false;
					break;
				}
				index++;
			}
			if (remove) {
				condition = condition.substring(1, condition.length() - 1);
			}
		}
		Filter previousFilter = null;
		boolean lastAnd = true;
		int left = 0;
		int right = 0;
		int functionLeft = 0;
		int functionRight = 0;
		int begin = 0;
		int end = 0;
		while (end <= condition.length()) {
			String token = condition.substring(end);
			if (token.startsWith(AND_TOKEN) && left == right) {
				String expr = condition.substring(begin, end);
				begin = end + AND_TOKEN.length();
				if (previousFilter == null) {
					if (expr.contains(AND_TOKEN) || expr.contains(OR_TOKEN)) {
						previousFilter = parseFilter(expr, context, parseContext);
					} else {
						previousFilter = getCommonFilter(expr, context, parseContext);
					}
				} else {
					Filter filter;
					if (expr.contains(AND_TOKEN) || expr.contains(OR_TOKEN)) {
						filter = parseFilter(expr, context, parseContext);
					} else {
						filter = getCommonFilter(expr, context, parseContext);
					}
					if (lastAnd) {
						previousFilter = new AndFilter(previousFilter, filter, (From) parseContext.get(FromParser.START_KEYWORD));
					} else {
						previousFilter = new OrFilter(previousFilter, filter, (From) parseContext.get(FromParser.START_KEYWORD));
					}
				}
				lastAnd = true;
			} else if (token.startsWith(OR_TOKEN) && left == right) {
				String expr = condition.substring(begin, end);
				begin = end + OR_TOKEN.length();
				if (previousFilter == null) {
					if (expr.contains(AND_TOKEN) || expr.contains(OR_TOKEN)) {
						previousFilter = parseFilter(expr, context, parseContext);
					} else {
						previousFilter = getCommonFilter(expr, context, parseContext);
					}
				} else {
					Filter filter;
					if (expr.contains(AND_TOKEN) || expr.contains(OR_TOKEN)) {
						filter = parseFilter(expr, context, parseContext);
					} else {
						filter = getCommonFilter(expr, context, parseContext);
					}
					if (lastAnd) {
						previousFilter = new AndFilter(previousFilter, filter, (From) parseContext.get(FromParser.START_KEYWORD));
					} else {
						previousFilter = new OrFilter(previousFilter, filter, (From) parseContext.get(FromParser.START_KEYWORD));
					}
				}
				lastAnd = false;
			} else if (token.startsWith("(")) {
				if (isAndOrLeftBracket(condition, 0, end)) {
					left++;
				} else {
					functionLeft++;
				}
			} else if (token.startsWith(")")) {
				if (isAndOrRightBracket(condition, end + 1, condition.length()) && functionLeft == functionRight) {
					right++;
				} else {
					functionRight++;
				}
			} else if (end == condition.length()) {
				String expr = condition.substring(begin, condition.length()).trim();
				if (StringUtils.isNotEmpty(expr)) {
					if (previousFilter == null) {
						previousFilter = getCommonFilter(condition, context, parseContext);
					} else {
						Filter filter;
						if (expr.contains(AND_TOKEN) || expr.contains(OR_TOKEN)) {
							filter = parseFilter(expr, context, parseContext);
						} else {
							filter = getCommonFilter(expr, context, parseContext);
						}
						if (lastAnd) {
							previousFilter = new AndFilter(previousFilter, filter, (From) parseContext.get(FromParser.START_KEYWORD));
						} else {
							previousFilter = new OrFilter(previousFilter, filter, (From) parseContext.get(FromParser.START_KEYWORD));
						}
					}
				}
			}
			end++;
		}

		return previousFilter;
	}

	private boolean isAndOrLeftBracket(String condition, int begin, int end) {
		int index = end - 1;
		if (index < begin) {
			return true;
		} else {
			while (index >= begin) {
				String token = condition.substring(index, end);
				if (token.equals("(")) {
					return true;
				} else if ((token.trim().equals(AND_TOKEN.trim()) && token.length() >= AND_TOKEN.length())
						|| (token.trim().equals(OR_TOKEN.trim()) && token.length() >= OR_TOKEN.length())) {
					return true;
				} else if (token.trim().length() == 0 && index == begin) {
					return true;
				}
				index--;
			}
			return false;
		}
	}

	private boolean isAndOrRightBracket(String condition, int begin, int end) {
		int index = begin + 1;
		if (index >= end) {
			return true;
		} else {
			while (index < end) {
				String token = condition.substring(begin, index);
				if (token.equals(")")) {
					return true;
				} else if ((token.trim().equals(AND_TOKEN.trim()) && token.length() >= AND_TOKEN.length())
						|| (token.trim().equals(OR_TOKEN.trim()) && token.length() >= OR_TOKEN.length())) {
					return true;
				} else if (token.trim().length() == 0 && index == begin) {
					return true;
				}
				index++;
			}
			return false;
		}
	}

	private Filter getCommonFilter(String expr, ProcessorContext context, Map<String, Object> parseContext) throws DplParseException {
		try {
			From from = (From) parseContext.get(FromParser.START_KEYWORD);
			for (CompareType compareType : CompareType.values()) {
				if (expr.contains(compareType.getOperation())) {
					expr = expr.trim();
					// if (log.isDebugEnabled()) {
					// log.debug("解析比较条件[" + expr + "]");
					// }
					String leftExpr = expr.substring(0, expr.indexOf(compareType.getOperation())).trim();
					String rightExpr = expr.substring(expr.indexOf(compareType.getOperation()) + compareType.getOperation().length(), expr.length()).trim();
					SingleRowFunction leftFunction = null;
					SingleRowFunction rightFunction = null;
					String leftVarName = null;
					String rightVarName = null;
					if (functionParser.containFunction(leftExpr)) {
						Function function = functionParser.parse(leftExpr, context, parseContext);
						if (function instanceof SingleRowFunction) {
							leftFunction = (SingleRowFunction) function;
						} else {
							throw new DplParseException("在where条件中不支持多行处理函数");
						}
					}

					if (functionParser.containFunction(rightExpr)) {
						Function function = functionParser.parse(rightExpr, context, parseContext);
						if (function instanceof SingleRowFunction) {
							rightFunction = (SingleRowFunction) function;
						} else {
							throw new DplParseException("在where条件中不支持多行处理函数");
						}
					}

					for (String name : from.getVariableNames()) {
						if (leftFunction != null) {
							List<String> fromParams = leftFunction.getFromParams();
							if (fromParams.size() > 1) {
								throw new DplParseException("在where条件中的函数中使用到的参数当且仅当包含一个from条件才可以");
							} else {
								if (fromParams.size() == 1) {
									leftVarName = fromParams.get(0);
								}
							}
						} else {
							if (leftExpr.startsWith(name + ".") || leftExpr.equals(name)) {
								leftVarName = name;
							}
						}
						if (rightFunction != null) {
							List<String> fromParams = rightFunction.getFromParams();
							if (fromParams.size() > 1) {
								throw new DplParseException("在where条件中的函数中使用到的参数当且仅当包含一个from条件才可以");
							} else {
								if (fromParams.size() == 1) {
									rightVarName = fromParams.get(0);
								}
							}
						} else {
							if (rightExpr.startsWith(name + ".") || rightExpr.equals(name)) {
								rightVarName = name;
							}
						}

					}
					PrefixAndSuffix leftPas;
					if (leftFunction != null) {
						leftPas = new PrefixAndSuffix(leftFunction);
					} else {
						leftPas = new PrefixAndSuffix(leftExpr, from, context);
					}
					PrefixAndSuffix rightPas;
					if (rightFunction != null) {
						rightPas = new PrefixAndSuffix(rightFunction);
					} else {
						rightPas = new PrefixAndSuffix(rightExpr, from, context);
					}
					return new CompareFilter(leftPas, rightPas, leftVarName, rightVarName, compareType, expr);
				}
			}
		} catch (Exception e) {
			throw new DplParseException(e);
		}
		throw new DplParseException("Cannt find compare type");
	}

	
	public Filter parse(String dpl, ProcessorContext context, Map<String, Object> parseContext) throws DplParseException {
		int index = dpl.indexOf(START_KEYWORD);
		if (index < 0) {
			return null;
		}
		int length = dpl.length();
		for (String endKeyword : END_KEYWORD) {
			int temp = dpl.indexOf(endKeyword);
			if (temp != -1 && temp > index && temp < length) {
				length = temp;
			}
		}
		index = index + START_KEYWORD.length();
		String whereDpl = dpl.substring(index, length).trim();
		Filter filter = parseFilter(whereDpl, context, parseContext);
		return filter;
	}

}

// $Id: FilterParser.java 13303 2010-05-27 03:46:01Z wei.zhang $