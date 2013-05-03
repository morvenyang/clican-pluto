package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UmpMbbGetbyidResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.ump.mbb.getbyid request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UmpMbbGetbyidRequest implements TaobaoRequest<UmpMbbGetbyidResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 积木块的id
	 */
	private Long id;

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return this.id;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.ump.mbb.getbyid";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("id", this.id);
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

	public Class<UmpMbbGetbyidResponse> getResponseClass() {
		return UmpMbbGetbyidResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(id,"id");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
