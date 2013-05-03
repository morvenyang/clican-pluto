package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 撤销运单结果
 *
 * @author auto create
 * @since 1.0, null
 */
public class CancelOrderResult extends TaobaoObject {

	private static final long serialVersionUID = 2621494628856899363L;

	/**
	 * 撤销后重新创建的订单的订单号
	 */
	@ApiField("recreate_order_id")
	private Long recreateOrderId;

	public Long getRecreateOrderId() {
		return this.recreateOrderId;
	}
	public void setRecreateOrderId(Long recreateOrderId) {
		this.recreateOrderId = recreateOrderId;
	}

}
