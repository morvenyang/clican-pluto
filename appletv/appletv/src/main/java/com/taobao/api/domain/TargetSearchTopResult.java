package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * TargetSearchTopResult
 *
 * @author auto create
 * @since 1.0, null
 */
public class TargetSearchTopResult extends TaobaoObject {

	private static final long serialVersionUID = 3556965215411419112L;

	/**
	 * 查询的指标名称
	 */
	@ApiField("field")
	private String field;

	/**
	 * 分页大小
	 */
	@ApiField("page_size")
	private Long pageSize;

	/**
	 * 所查询的指标数据的结果
	 */
	@ApiField("result_data")
	private String resultData;

	/**
	 * 分页的数据总数
	 */
	@ApiField("total_count")
	private Long totalCount;

	public String getField() {
		return this.field;
	}
	public void setField(String field) {
		this.field = field;
	}

	public Long getPageSize() {
		return this.pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public String getResultData() {
		return this.resultData;
	}
	public void setResultData(String resultData) {
		this.resultData = resultData;
	}

	public Long getTotalCount() {
		return this.totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

}
