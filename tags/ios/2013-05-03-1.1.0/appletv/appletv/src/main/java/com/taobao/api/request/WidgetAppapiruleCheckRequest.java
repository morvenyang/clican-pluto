package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.WidgetAppapiruleCheckResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.widget.appapirule.check request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class WidgetAppapiruleCheckRequest implements TaobaoRequest<WidgetAppapiruleCheckResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 要判断权限的api名称，如果指定的api不存在，报错invalid method
	 */
	private String apiName;

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getApiName() {
		return this.apiName;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.widget.appapirule.check";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("api_name", this.apiName);
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

	public Class<WidgetAppapiruleCheckResponse> getResponseClass() {
		return WidgetAppapiruleCheckResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(apiName,"apiName");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
