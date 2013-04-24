package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.WlbWlborderGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.wlb.wlborder.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class WlbWlborderGetRequest implements TaobaoRequest<WlbWlborderGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 物流宝订单编码
	 */
	private String wlbOrderCode;

	public void setWlbOrderCode(String wlbOrderCode) {
		this.wlbOrderCode = wlbOrderCode;
	}
	public String getWlbOrderCode() {
		return this.wlbOrderCode;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.wlb.wlborder.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("wlb_order_code", this.wlbOrderCode);
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

	public Class<WlbWlborderGetResponse> getResponseClass() {
		return WlbWlborderGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(wlbOrderCode,"wlbOrderCode");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
