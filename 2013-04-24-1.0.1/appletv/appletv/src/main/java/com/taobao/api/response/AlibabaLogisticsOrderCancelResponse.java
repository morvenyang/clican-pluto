package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.CancelOrderResult;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: alibaba.logistics.order.cancel response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class AlibabaLogisticsOrderCancelResponse extends TaobaoResponse {

	private static final long serialVersionUID = 6343794288211488876L;

	/** 
	 * 撤销物流订单结果
	 */
	@ApiField("cancel_order_result")
	private CancelOrderResult cancelOrderResult;

	public void setCancelOrderResult(CancelOrderResult cancelOrderResult) {
		this.cancelOrderResult = cancelOrderResult;
	}
	public CancelOrderResult getCancelOrderResult( ) {
		return this.cancelOrderResult;
	}

}
