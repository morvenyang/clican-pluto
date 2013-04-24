/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.enumeration;

/**
 * 在工作流内部系统内用到的一些参数变量名的定义
 * 
 * @author wei.zhang
 * 
 */
public enum Parameters {

	FIRST_PRIORITY_ASSIGNEE("firstPriorityAssignee"),

	VOTE_RESULT("voteResult"),

	AUDIT_SUGGESTION("auditSuggestion"),

	TASK_ID("taskId"),

	SPONSOR("sponsor"),

	VOTED_AGREE_NUMBER("votedAgreeNumber"),

	NOTICE_IMMEDIATELY("noticeImmediately"),

	CANCELED_STATE_NAMES("canceledStateNames");

	private String parameter;

	private Parameters(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}

// $Id$