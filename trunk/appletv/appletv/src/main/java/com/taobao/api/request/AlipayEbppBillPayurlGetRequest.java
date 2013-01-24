package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.AlipayEbppBillPayurlGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: alipay.ebpp.bill.payurl.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class AlipayEbppBillPayurlGetRequest implements TaobaoRequest<AlipayEbppBillPayurlGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 支付宝的业务订单号，具有唯一性。
	 */
	private String alipayOrderNo;

	/** 
	* 如有有淘宝授权的session可以不传这个字段
	 */
	private String authToken;

	/** 
	* 输出机构的业务流水号，需要保证唯一性。
	 */
	private String merchantOrderNo;

	/** 
	* 支付宝订单类型。公共事业缴纳JF,信用卡还款HK。
	 */
	private String orderType;

	public void setAlipayOrderNo(String alipayOrderNo) {
		this.alipayOrderNo = alipayOrderNo;
	}
	public String getAlipayOrderNo() {
		return this.alipayOrderNo;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getAuthToken() {
		return this.authToken;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}
	public String getMerchantOrderNo() {
		return this.merchantOrderNo;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderType() {
		return this.orderType;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "alipay.ebpp.bill.payurl.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("alipay_order_no", this.alipayOrderNo);
		txtParams.put("auth_token", this.authToken);
		txtParams.put("merchant_order_no", this.merchantOrderNo);
		txtParams.put("order_type", this.orderType);
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

	public Class<AlipayEbppBillPayurlGetResponse> getResponseClass() {
		return AlipayEbppBillPayurlGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(alipayOrderNo,"alipayOrderNo");
		RequestCheckUtils.checkMaxLength(alipayOrderNo,28,"alipayOrderNo");
		RequestCheckUtils.checkNotEmpty(merchantOrderNo,"merchantOrderNo");
		RequestCheckUtils.checkMaxLength(merchantOrderNo,32,"merchantOrderNo");
		RequestCheckUtils.checkNotEmpty(orderType,"orderType");
		RequestCheckUtils.checkMaxLength(orderType,10,"orderType");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
