/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.enumeration;

/**
 * 枚举了在DataProcess各个节点中的Transaction模式。
 * <p>
 * 默认还有一个TransactionMode就是没有Transaction
 * 
 * @author wei.zhang
 * 
 */
public enum TransactionMode {

	/**
	 * 从该节点开始一个新的Transaction
	 */
	BEGIN("begin"),

	/**
	 * 从该节点结束当前的Transaction
	 */
	END("end"),

	/**
	 * 单节点单个Transaction
	 */
	COMMIT("commit");

	private String mode;

	private TransactionMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public static TransactionMode convert(String mode) {
		for (TransactionMode member : values()) {
			if (member.getMode().equals(mode)) {
				return member;
			}
		}
		return null;
	}
}

// $Id$