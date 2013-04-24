package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.TargetSearchTopResult;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.udp.item.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UdpItemGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4254271496166699987L;

	/** 
	 * 指标查询返回结果
	 */
	@ApiField("content")
	private TargetSearchTopResult content;

	public void setContent(TargetSearchTopResult content) {
		this.content = content;
	}
	public TargetSearchTopResult getContent( ) {
		return this.content;
	}

}
