package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UmpDetailUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.ump.detail.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UmpDetailUpdateRequest implements TaobaoRequest<UmpDetailUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 活动详情内容，可以通过ump sdk中的MarketingTool来生成这个内容
	 */
	private String content;

	/** 
	* 活动详情id
	 */
	private Long detailId;

	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return this.content;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public Long getDetailId() {
		return this.detailId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.ump.detail.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("content", this.content);
		txtParams.put("detail_id", this.detailId);
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

	public Class<UmpDetailUpdateResponse> getResponseClass() {
		return UmpDetailUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(content,"content");
		RequestCheckUtils.checkNotEmpty(detailId,"detailId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
