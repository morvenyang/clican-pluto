package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.caipiao.lottery.send response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class CaipiaoLotterySendResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8281244334867737852L;

	/** 
	 * 赠送是否成功，成功为true, 否则为false
	 */
	@ApiField("send_result")
	private Boolean sendResult;

	public void setSendResult(Boolean sendResult) {
		this.sendResult = sendResult;
	}
	public Boolean getSendResult( ) {
		return this.sendResult;
	}

}
