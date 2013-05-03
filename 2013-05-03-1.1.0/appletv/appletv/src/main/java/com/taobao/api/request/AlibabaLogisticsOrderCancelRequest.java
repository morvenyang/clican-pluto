package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.AlibabaLogisticsOrderCancelResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: alibaba.logistics.order.cancel request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class AlibabaLogisticsOrderCancelRequest implements TaobaoRequest<AlibabaLogisticsOrderCancelResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 物流订单id
	 */
	private Long orderId;

	/** 
	* 撤销原因说明。
	 */
	private String reason;

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getOrderId() {
		return this.orderId;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReason() {
		return this.reason;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "alibaba.logistics.order.cancel";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("order_id", this.orderId);
		txtParams.put("reason", this.reason);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new TaobaoHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<AlibabaLogisticsOrderCancelResponse> getResponseClass() {
		return AlibabaLogisticsOrderCancelResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(orderId,"orderId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
