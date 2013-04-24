package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TripJipiaoAgentOrderSpecialConfirmResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.trip.jipiao.agent.order.special.confirm request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TripJipiaoAgentOrderSpecialConfirmRequest implements TaobaoRequest<TripJipiaoAgentOrderSpecialConfirmResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 能否支付
	 */
	private Boolean canPay;

	/** 
	* can_pay=false,fail_type=0时，必需提供失败原因
	 */
	private String failMemo;

	/** 
	* can_pay=false时，必需提供失败原因；失败原因：11,座位已售完;12,座位申请不成功;13,航班价格变动;14,买家要求失败订单;0,其它(必须在备注中说明原因)
	 */
	private Long failType;

	/** 
	* 国内机票订单id
	 */
	private Long orderId;

	/** 
	* can_pay=true，必需提供最迟支付时间
	 */
	private Date payLatestTime;

	public void setCanPay(Boolean canPay) {
		this.canPay = canPay;
	}
	public Boolean getCanPay() {
		return this.canPay;
	}

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

	public void setPayLatestTime(Date payLatestTime) {
		this.payLatestTime = payLatestTime;
	}
	public Date getPayLatestTime() {
		return this.payLatestTime;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.trip.jipiao.agent.order.special.confirm";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("can_pay", this.canPay);
		txtParams.put("fail_memo", this.failMemo);
		txtParams.put("fail_type", this.failType);
		txtParams.put("order_id", this.orderId);
		txtParams.put("pay_latest_time", this.payLatestTime);
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

	public Class<TripJipiaoAgentOrderSpecialConfirmResponse> getResponseClass() {
		return TripJipiaoAgentOrderSpecialConfirmResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(canPay,"canPay");
		RequestCheckUtils.checkMaxLength(failMemo,200,"failMemo");
		RequestCheckUtils.checkNotEmpty(orderId,"orderId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
