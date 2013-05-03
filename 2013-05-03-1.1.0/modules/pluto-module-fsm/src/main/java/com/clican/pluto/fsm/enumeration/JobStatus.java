/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.enumeration;

public enum JobStatus {

	/**
	 * 空闲状态,在数据库中未被处理
	 */
	IDLE("idle"),

	/**
	 * 在内存中等待执行或正在执行
	 */
	EXECUTING("executing"),

	/**
	 * 执行完成
	 */
	EXECUTED("executed");
	
	private String status;
	
	private JobStatus(String status){
		this.status= status;
	}

	public String getStatus() {
		return status;
	}
	
}


// $Id$