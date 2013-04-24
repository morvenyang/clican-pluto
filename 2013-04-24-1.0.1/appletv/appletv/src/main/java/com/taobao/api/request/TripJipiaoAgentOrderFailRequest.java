package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TripJipiaoAgentOrderFailResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.trip.jipiao.agent.order.fail request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TripJipiaoAgentOrderFailRequest implements TaobaoRequest<TripJipiaoAgentOrderFailResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 失败类型为0，说明备注原因
	 */
	private String failMemo;

	/** 
	* 失败原因：1．客户要求失败订单；2．此舱位已售完（经济舱或特舱）；3．剩余座位少于用户购买数量；4．特价管理里录入的特价票已经售完；5．假舱（请及时通过旺旺或者电话反馈给淘宝工作人员）；0．其它（请在备注中说明原因）
	 */
	private Long failType;

	/** 
	* 国内机票订单id
	 */
	private Long orderId;

	public void setFailMemo(String failMemo) {
		this.failMemo = failMemo;
	}
	public String getFailMemo() {
		return this.failMemo;
	}

	public void setFailType(Long failType) {
		this.failType = failType;
	}
	public Long getFailType() {
		return this.failType;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getOrderId() {
		return this.orderId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.trip.jipiao.agent.order.fail";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("fail_memo", this.failMemo);
		txtParams.put("fail_type", this.failType);
		txtParams.put("order_id", this.orderId);
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

	public Class<TripJipiaoAgentOrderFailResponse> getResponseClass() {
		return TripJipiaoAgentOrderFailResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkMaxLength(failMemo,200,"failMemo");
		RequestCheckUtils.checkNotEmpty(failType,"failType");
		RequestCheckUtils.checkNotEmpty(orderId,"orderId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
