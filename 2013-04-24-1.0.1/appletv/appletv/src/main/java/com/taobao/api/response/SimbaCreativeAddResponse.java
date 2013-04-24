package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Creative;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.creative.add response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCreativeAddResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8489995758758377365L;

	/** 
	 * 新增加的创意对象
	 */
	@ApiField("creative")
	private Creative creative;

	public void setCreative(Creative creative) {
		this.creative = creative;
	}
	public Creative getCreative( ) {
		return this.creative;
	}

}
