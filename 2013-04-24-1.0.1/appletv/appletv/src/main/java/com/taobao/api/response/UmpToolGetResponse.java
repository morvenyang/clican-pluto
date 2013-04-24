package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.ump.tool.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UmpToolGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5216639672587447329L;

	/** 
	 * 工具信息内容，格式为json，可以通过提供给的sdk里面的MarketingBuilder来处理这个内容
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
