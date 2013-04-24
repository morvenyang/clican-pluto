package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.OrderCharge;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: alibaba.logistics.order.charge response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class AlibabaLogisticsOrderChargeResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3119385291793324582L;

	/** 
	 * 订单费用
	 */
	@ApiField("order_charge")
	private OrderCharge orderCharge;

	public void setOrderCharge(OrderCharge orderCharge) {
		this.orderCharge = orderCharge;
	}
	public OrderCharge getOrderCharge( ) {
		return this.orderCharge;
	}

}
