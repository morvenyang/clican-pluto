/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.dpl.function.impl.BaseFunction;
import com.clican.pluto.dataprocess.dpl.function.impl.Divide;
import com.clican.pluto.dataprocess.dpl.function.impl.Great;
import com.clican.pluto.dataprocess.dpl.function.impl.Less;
import com.clican.pluto.dataprocess.dpl.function.impl.Minus;
import com.clican.pluto.dataprocess.dpl.function.impl.Mod;
import com.clican.pluto.dataprocess.dpl.function.impl.Multi;
import com.clican.pluto.dataprocess.dpl.function.impl.Plus;
import com.clican.pluto.dataprocess.dpl.function.impl.Pow;
import com.clican.pluto.dataprocess.dpl.parser.DplParser;
import com.clican.pluto.dataprocess.dpl.parser.eunmeration.FunctionOperation;
import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 用来解析select或where语句中function部分的解析器
 * 
 * @author wei.zhang
 * 
 */
public class FunctionParser implements DplParser {

	public final static String FUNCTION_PACKAGE = BaseFunction.class.getPackage().getName();

	public final static String PARAM_SPLIT_EXPR = ",";

	public final static Log log = LogFactory.getLog(FunctionParser.class);

	public static final String START_KEYWORD = "(";

	public final static String END_KEYWORD = ")";

	private String[] extFunctionPackage;

	public void setExtFunctionPackage(String[] extFunctionPackage) {
		this.extFunctionPackage = extFunctionPackage;
	}

	public boolean containFunction(String dpl) {
		dpl = dpl.trim();
		if (dpl.startsWith("'") && dpl.startsWith("'")) {
			return false;
		}
		if (dpl.contains(FunctionParser.START_KEYWORD) && dpl.contains(FunctionParser.END_KEYWORD)) {
			return true;
		} else if (FunctionOperation.containOperation(dpl)) {
			return true;
		} else {
			return false;
		}
	}

	private String processPriority(String dpl) throws DplParseException {
		dpl = dpl.trim();
		String[] parts = dpl.split("[\\*\\+\\-\\/\\%\\^]");
		if (parts.length == 1) {
			return dpl;
		}
		String dplCopy = dpl;
		for (int i = 0; i < parts.length; i++) {
			int index = dplCopy.indexOf(parts[i]);
			dplCopy = dplCopy.substring(0, index) + " " + dplCopy.substring(index + parts[i].length());
		}
		String[] operations = dplCopy.split(" ");
		List<String> partList = new ArrayList<String>();
		int left = 0;
		int right = 0;
		boolean append = false;
		for (int i = 0; i < parts.length; i++) {
			String part = (operations[i] + parts[i]).trim();

			left += StringUtils.countMatches(parts[i].trim(), "(");

			right += StringUtils.countMatches(parts[i].trim(), ")");

			if (left == right && StringUtils.countMatches(parts[i].trim(), "(") == StringUtils.countMatches(parts[i].trim(), ")")) {
				partList.add(part);
				left = 0;
				right = 0;
			} else if (left > right) {
				if (!append) {
					partList.add(part);
					append = true;
				} else {
					String lastOne = partList.remove(partList.size() - 1);
					lastOne += part;
					partList.add(lastOne);
				}
			} else if (left == right) {
				String lastOne = partList.remove(partList.size() - 1);
				lastOne += part;
				partList.add(lastOne);
				left = 0;
				right = 0;
				append = false;
			} else {
				throw new DplParseException("在表达式中[" + dpl + "]左右括号不对称");
			}
		}
		if (left != 0 || right != 0) {
			throw new DplParseException("在表达式中[" + dpl + "]左右括号不对称");
		}
		String result = "";
		String previousOne = null;
		if (partList.size() == 1) {
			result = partList.get(0);
			if (StringUtils.countMatches(result, "(") > 0 && StringUtils.countMatches(result, "(") > 0) {
				return result;
			} else {
				return "(" + result + ")";
			}
		}
		for (int i = 0; i < partList.size(); i++) {
			String part = partList.get(i);

			if (part.startsWith("*") || part.startsWith("%") || part.startsWith("/") || part.startsWith("^")) {
				if (previousOne == null) {
					throw new DplParseException("函数解析错误[" + dpl + "]");
				}
				if (FunctionOperation.containOperation(previousOne.substring(0, 1))) {
					previousOne = previousOne.substring(0, 1) + "(" + previousOne.substring(1) + part + ")";
				} else {
					previousOne = "(" + previousOne + part + ")";
				}
				if (i == partList.size() - 1) {
					if (StringUtils.isEmpty(result)) {
						result = previousOne;
					} else {
						result = "(" + result + previousOne + ")";
					}
				}
			} else {
				if (part.startsWith("+") || part.startsWith("-")) {
					result += previousOne;
				}
				previousOne = part;
				if (i == partList.size() - 1) {
					result = "(" + result + part + ")";
				}
			}
		}
		return result;
	}

	private String removeUnused(String expr) {
		if (expr.startsWith("(") && expr.endsWith(")")) {
			int left = 0;
			int right = 0;
			int index = 0;
			while (index < expr.length()) {
				String token = expr.substring(index, index + 1);
				if (token.equals("(")) {
					left++;
				}
				if (token.equals(")")) {
					right++;
				}
				if (left == right) {
					if (index == expr.length() - 1) {
						String result = expr.substring(1, expr.length() - 1);
						if (result.startsWith("(") && result.endsWith(")")) {
							return removeUnused(result);
						} else {
							return result;
						}
					} else {
						return expr;
					}

				}
				index++;
			}
			return expr;
		} else {
			return expr;
		}

	}

	public Function parse(String dpl, ProcessorContext context) throws DplParseException {
		String copy = dpl;
		if (StringUtils.isEmpty(dpl)) {
			throw new DplParseException("解析函数出错，被解析内容为空");
		}
		dpl = removeUnused(dpl);
		dpl = processPriority(dpl);
		if (!dpl.contains(START_KEYWORD) && !dpl.contains(END_KEYWORD)) {
			throw new DplParseException("解析函数出错，函数提必须包括函数名和'" + START_KEYWORD + "'" + PARAM_SPLIT_EXPR + "'" + END_KEYWORD + "'");
		}
		// if (log.isDebugEnabled()) {
		// log.debug("parse function[" + dpl.replaceAll("\n", "") + "]");
		// }
		try {
			String functionName = dpl.substring(0, dpl.indexOf(START_KEYWORD)).trim();
			if (StringUtils.isEmpty(functionName)) {
				int index = 0;
				int leftKey = 0;
				int rightRight = 0;
				while (index < dpl.length()) {
					String s = dpl.substring(index, index + 1);
					if (s.equals(START_KEYWORD)) {
						leftKey++;
					}
					if (s.equals(END_KEYWORD)) {
						rightRight++;
					}
					if (leftKey - rightRight == 1) {
						Function function = null;
						String[] params = new String[2];
						if (s.equals("+")) {
							function = new Plus();
						} else if (s.equals("-")) {
							function = new Minus();
						} else if (s.equals("/")) {
							function = new Divide();
						} else if (s.equals("*")) {
							function = new Multi();
						} else if (s.equals("<")) {
							function = new Less();
						} else if (s.equals(">")) {
							function = new Great();
						} else if (s.equals("^")) {
							function = new Pow();
						} else if (s.equals("%")) {
							function = new Mod();
						} else {
							index++;
							continue;
						}
						String paramExpr = dpl.substring(dpl.indexOf(FunctionParser.START_KEYWORD) + 1, dpl.lastIndexOf(FunctionParser.END_KEYWORD));
						params[0] = paramExpr.substring(0, index - 1).trim();
						params[1] = paramExpr.substring(index).trim();
						String param = "";
						List<Object> paramList = new ArrayList<Object>();
						for (int i = 0; i < params.length; i++) {
							if (StringUtils.isEmpty(param)) {
								param = params[i];
							} else {
								param += "," + params[i];
							}
							int left = StringUtils.countMatches(param, START_KEYWORD);
							int right = StringUtils.countMatches(param, END_KEYWORD);
							boolean containOperation = FunctionOperation.containOperation(param);
							if (left == 0 && right == 0 && !containOperation) {
								paramList.add(param);
								param = "";
							} else if (left == right) {
								paramList.add(this.parse(param, context));
								param = "";
							} else if (i == params.length - 1) {
								throw new DplParseException("函数解析错误()不对称");
							}
						}
						function.setExpr(copy);
						function.setParams(paramList, context);
						return function;
					}
					index++;
				}
				throw new DplParseException("函数解析错误()不对称");
			} else {
				functionName = functionName.substring(0, 1).toUpperCase() + functionName.substring(1);
				String functionTrace = null;
				if (functionName.contains("=>")) {
					functionTrace = functionName.split("=>")[1];
					functionName = functionName.split("=>")[0];
				}
				Class<?> clazz = null;

				try {
					clazz = Class.forName(FUNCTION_PACKAGE + "." + functionName);
				} catch (ClassNotFoundException e) {
					for (String extPackage : extFunctionPackage) {
						try {
							clazz = Class.forName(extPackage + "." + functionName);
						} catch (ClassNotFoundException ex) {

						}
					}
					if (clazz == null) {
						throw e;
					}
				}

				Function function = (Function) clazz.newInstance();
				function.setTrace(functionTrace);
				String paramExpr = dpl.substring(dpl.indexOf(FunctionParser.START_KEYWORD) + 1, dpl.lastIndexOf(FunctionParser.END_KEYWORD));
				String[] params = paramExpr.split(PARAM_SPLIT_EXPR);
				String param = "";
				List<Object> paramList = new ArrayList<Object>();
				for (int i = 0; i < params.length; i++) {
					if (StringUtils.isEmpty(param)) {
						param = params[i].trim();
					} else {
						param += "," + params[i].trim();
					}
					int left = StringUtils.countMatches(param, START_KEYWORD);
					int right = StringUtils.countMatches(param, END_KEYWORD);
					boolean containOperation = FunctionOperation.containOperation(param);
					if (left == 0 && right == 0 && !containOperation) {
						paramList.add(param);
						param = "";
					} else if (left == right) {
						paramList.add(this.parse(param, context));
						param = "";
					} else if (i == params.length - 1) {
						throw new DplParseException("函数解析错误()不对称");
					}
				}
				function.setParams(paramList, context);
				function.setExpr(copy);
				return function;
			}
		} catch (Exception e) {
			throw new DplParseException("expr[" + copy + "]", e);
		}

	}

}

// $Id: FunctionParser.java 14982 2010-06-18 00:37:00Z wei.zhang $