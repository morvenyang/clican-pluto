package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.FenxiaoOrderMessageAddResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.fenxiao.order.message.add request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class FenxiaoOrderMessageAddRequest implements TaobaoRequest<FenxiaoOrderMessageAddResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 留言内容
	 */
	private String message;

	/** 
	* 采购单id
	 */
	private Long purchaseOrderId;

	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return this.message;
	}

	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public Long getPurchaseOrderId() {
		return this.purchaseOrderId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.fenxiao.order.message.add";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("message", this.message);
		txtParams.put("purchase_order_id", this.purchaseOrderId);
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

	public Class<FenxiaoOrderMessageAddResponse> getResponseClass() {
		return FenxiaoOrderMessageAddResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(message,"message");
		RequestCheckUtils.checkNotEmpty(purchaseOrderId,"purchaseOrderId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
