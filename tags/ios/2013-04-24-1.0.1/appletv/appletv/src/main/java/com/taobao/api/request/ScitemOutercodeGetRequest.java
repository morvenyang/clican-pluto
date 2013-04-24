package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.ScitemOutercodeGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.scitem.outercode.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class ScitemOutercodeGetRequest implements TaobaoRequest<ScitemOutercodeGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 商品编码
	 */
	private String outerCode;

	public void setOuterCode(String outerCode) {
		this.outerCode = outerCode;
	}
	public String getOuterCode() {
		return this.outerCode;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.scitem.outercode.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("outer_code", this.outerCode);
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

	public Class<ScitemOutercodeGetResponse> getResponseClass() {
		return ScitemOutercodeGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(outerCode,"outerCode");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
