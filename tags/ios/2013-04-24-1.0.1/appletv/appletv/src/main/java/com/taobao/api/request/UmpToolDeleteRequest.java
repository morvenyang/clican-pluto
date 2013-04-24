package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UmpToolDeleteResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.ump.tool.delete request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UmpToolDeleteRequest implements TaobaoRequest<UmpToolDeleteResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 营销工具id
	 */
	private Long toolId;

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}
	public Long getToolId() {
		return this.toolId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.ump.tool.delete";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("tool_id", this.toolId);
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

	public Class<UmpToolDeleteResponse> getResponseClass() {
		return UmpToolDeleteResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(toolId,"toolId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
