package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.ConsignResult;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: alibaba.logistics.order.consign response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class AlibabaLogisticsOrderConsignResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3224265956166638249L;

	/** 
	 * 发货结果
	 */
	@ApiField("consign_result")
	private ConsignResult consignResult;

	public void setConsignResult(ConsignResult consignResult) {
		this.consignResult = consignResult;
	}
	public ConsignResult getConsignResult( ) {
		return this.consignResult;
	}

}
