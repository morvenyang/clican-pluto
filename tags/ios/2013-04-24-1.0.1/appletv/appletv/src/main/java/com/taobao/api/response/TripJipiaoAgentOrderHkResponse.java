package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.trip.jipiao.agent.order.hk response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TripJipiaoAgentOrderHkResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3648895645336988474L;

	/** 
	 * 手工HK成功失败
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Boolean getIsSuccess( ) {
		return this.isSuccess;
	}

}
