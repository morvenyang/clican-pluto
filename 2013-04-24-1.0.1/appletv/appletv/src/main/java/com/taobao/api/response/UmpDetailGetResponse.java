package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.ump.detail.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UmpDetailGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8858471197122283285L;

	/** 
	 * 活动详情信息，可以通过ump sdk中的MarketingTool来进行处理
	 */
	@ApiField("content")
	private String content;

	public void setContent(String content) {
		this.content = content;
	}
	public String getContent( ) {
		return this.content;
	}

}
