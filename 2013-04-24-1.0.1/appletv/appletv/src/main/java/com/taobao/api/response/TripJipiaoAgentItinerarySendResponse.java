package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.trip.jipiao.agent.itinerary.send response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TripJipiaoAgentItinerarySendResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2818622868365979653L;

	/** 
	 * 成功或者失败
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
