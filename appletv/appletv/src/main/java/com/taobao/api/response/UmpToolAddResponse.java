package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.ump.tool.add response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UmpToolAddResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1335259239671564274L;

	/** 
	 * 工具id
	 */
	@ApiField("tool_id")
	private Long toolId;

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}
	public Long getToolId( ) {
		return this.toolId;
	}

}
