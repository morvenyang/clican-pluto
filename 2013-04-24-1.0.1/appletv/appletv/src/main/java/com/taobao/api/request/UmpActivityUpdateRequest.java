package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UmpActivityUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.ump.activity.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UmpActivityUpdateRequest implements TaobaoRequest<UmpActivityUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 活动id
	 */
	private Long actId;

	/** 
	* 营销活动内容，json格式，通过ump sdk 的marketingTool来生成
	 */
	private String content;

	public void setActId(Long actId) {
		this.actId = actId;
	}
	public Long getActId() {
		return this.actId;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return this.content;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.ump.activity.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("act_id", this.actId);
		txtParams.put("content", this.content);
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

	public Class<UmpActivityUpdateResponse> getResponseClass() {
		return UmpActivityUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(actId,"actId");
		RequestCheckUtils.checkNotEmpty(content,"content");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
