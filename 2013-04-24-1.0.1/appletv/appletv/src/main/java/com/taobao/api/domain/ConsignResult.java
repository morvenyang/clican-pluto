package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 发货返回结果
 *
 * @author auto create
 * @since 1.0, null
 */
public class ConsignResult extends TaobaoObject {

	private static final long serialVersionUID = 8618376298594634972L;

	/**
	 * 物流编号，作为业务标识字段。
	 */
	@ApiField("logistics_id")
	private String logisticsId;

	/**
	 * 订单号
	 */
	@ApiField("order_id")
	private Long orderId;

	public String getLogisticsId() {
		return this.logisticsId;
	}
	public void setLogisticsId(String logisticsId) {
		this.logisticsId = logisticsId;
	}

	public Long getOrderId() {
		return this.orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

}
