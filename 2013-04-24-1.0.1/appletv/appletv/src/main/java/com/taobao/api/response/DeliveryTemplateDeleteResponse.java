package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.delivery.template.delete response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class DeliveryTemplateDeleteResponse extends TaobaoResponse {

	private static final long serialVersionUID = 6787997493881579173L;

	/** 
	 * 表示删除成功还是失败
	 */
	@ApiField("complete")
	private Boolean complete;

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}
	public Boolean getComplete( ) {
		return this.complete;
	}

}
