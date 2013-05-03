package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.ump.activity.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UmpActivityGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5484524999699753932L;

	/** 
	 * 营销活动的内容，可以通过ump sdk中的marketingTool来完成对该内容的处理
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
