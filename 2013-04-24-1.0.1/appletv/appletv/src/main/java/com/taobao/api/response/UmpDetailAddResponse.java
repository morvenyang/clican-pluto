package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.ump.detail.add response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UmpDetailAddResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1348648851777863143L;

	/** 
	 * 活动详情的id
	 */
	@ApiField("detail_id")
	private Long detailId;

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public Long getDetailId( ) {
		return this.detailId;
	}

}
