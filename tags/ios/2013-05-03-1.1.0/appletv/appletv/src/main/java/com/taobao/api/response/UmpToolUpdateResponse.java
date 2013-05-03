package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.ump.tool.update response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UmpToolUpdateResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5423667852719916182L;

	/** 
	 * 更新后生成的新的工具id
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
