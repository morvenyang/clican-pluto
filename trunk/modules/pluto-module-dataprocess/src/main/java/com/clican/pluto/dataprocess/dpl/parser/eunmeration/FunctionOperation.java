/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.eunmeration;

/**
 * 在select语句中使用的数学运算符号
 * 
 * @author wei.zhang
 * 
 */
public enum FunctionOperation {

	/**
	 * 执行加法操作
	 */
	PLUS("+"),

	/**
	 * 执行减法操作
	 */
	MINUS("-"),

	/**
	 * 执行乘法操作
	 */
	MULTI("*"),

	/**
	 * 执行除法操作
	 */
	DIVIDE("/"),

	/**
	 * 执行求余操作
	 */
	MOD("%"),

	/**
	 * 执行乘积操作
	 */
	POWER("^");

	private String operation;

	private FunctionOperation(String operation) {
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}

	public static FunctionOperation convert(String operation) {
		for (FunctionOperation member : values()) {
			if (member.operation.equals(operation)) {
				return member;
			}
		}
		return null;
	}

	/**
	 * 是否包含乘法、除法、求余和求积
	 * 
	 * @param expr
	 * @return
	 */
	public static boolean containFirstPriorityOperation(String expr) {
		for (FunctionOperation member : new FunctionOperation[] { MULTI, DIVIDE, MOD, POWER }) {
			// 排除a.*的情况
			if (expr.contains(member.operation) && !expr.contains("." + member.operation)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否包含加法和减法
	 * 
	 * @param expr
	 * @return
	 */
	public static boolean containSecondPriorityOperation(String expr) {
		for (FunctionOperation member : new FunctionOperation[] { PLUS, MINUS }) {
			// 排除a.*的情况
			if (expr.contains(member.operation) && !expr.contains("." + member.operation)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否包含数学运算符号
	 * 
	 * @param expr
	 * @return
	 */
	public static boolean containOperation(String expr) {
		for (FunctionOperation member : values()) {
			// 排除a.*的情况
			if (expr.contains(member.operation) && !expr.contains("." + member.operation) && !(expr.startsWith("'") && expr.endsWith("'"))) {
				return true;
			}
		}
		return false;
	}

}

// $Id: FunctionOperation.java 16231 2010-07-16 03:15:31Z wei.zhang $