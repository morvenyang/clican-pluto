package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.FenxiaoOrderCustomfieldUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.fenxiao.order.customfield.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class FenxiaoOrderCustomfieldUpdateRequest implements TaobaoRequest<FenxiaoOrderCustomfieldUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 自定义key
	 */
	private String isvCustomKey;

	/** 
	* 自定义的值
	 */
	private String isvCustomValue;

	/** 
	* 采购单id
	 */
	private Long purchaseOrderId;

	public void setIsvCustomKey(String isvCustomKey) {
		this.isvCustomKey = isvCustomKey;
	}
	public String getIsvCustomKey() {
		return this.isvCustomKey;
	}

	public void setIsvCustomValue(String isvCustomValue) {
		this.isvCustomValue = isvCustomValue;
	}
	public String getIsvCustomValue() {
		return this.isvCustomValue;
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
		return "taobao.fenxiao.order.customfield.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("isv_custom_key", this.isvCustomKey);
		txtParams.put("isv_custom_value", this.isvCustomValue);
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

	public Class<FenxiaoOrderCustomfieldUpdateResponse> getResponseClass() {
		return FenxiaoOrderCustomfieldUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(isvCustomKey,"isvCustomKey");
		RequestCheckUtils.checkNotEmpty(purchaseOrderId,"purchaseOrderId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
