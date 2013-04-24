/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.orm.enumeration;

import com.clican.pluto.common.inter.LabelAndValue;

public enum IssueStatus implements LabelAndValue{

	NOT_ISSUE(0, "未发布"),

	ISSUING(1, "发布中"),

	ISSUE_SUCCESS(2, "发布成功"),

	ISSUE_FAILURE(3, "发布失败");

	private int status;
	
	private String label;
	
	private IssueStatus(int status,String label){
		this.status = status;
		this.label = label;
	}

	public Integer getValue() {
		return status;
	}

	public String getLabel() {
		return label;
	}
	
	
}

// $Id$