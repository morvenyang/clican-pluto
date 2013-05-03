package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 部门信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class Department extends TaobaoObject {

	private static final long serialVersionUID = 7248419791716647195L;

	/**
	 * 部门ID
	 */
	@ApiField("department_id")
	private Long departmentId;

	/**
	 * 部门名称
	 */
	@ApiField("department_name")
	private String departmentName;

	/**
	 * 当前部门的父部门ID
	 */
	@ApiField("parent_id")
	private Long parentId;

	public Long getDepartmentId() {
		return this.departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return this.departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getParentId() {
		return this.parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
