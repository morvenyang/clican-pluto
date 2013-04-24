package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TripJipiaoAgentOrderGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.trip.jipiao.agent.order.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TripJipiaoAgentOrderGetRequest implements TaobaoRequest<TripJipiaoAgentOrderGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 淘宝政策id列表，当前支持列表长度为1，即当前只支持单个订单详情查询
	 */
	private String orderIds;

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}
	public String getOrderIds() {
		return this.orderIds;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.trip.jipiao.agent.order.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("order_ids", this.orderIds);
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

	public Class<TripJipiaoAgentOrderGetResponse> getResponseClass() {
		return TripJipiaoAgentOrderGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(orderIds,"orderIds");
		RequestCheckUtils.checkMaxListSize(orderIds,1,"orderIds");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
