package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UmpDetailGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.ump.detail.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UmpDetailGetRequest implements TaobaoRequest<UmpDetailGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 活动详情的id
	 */
	private Long detailId;

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
		return "taobao.ump.detail.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
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

	public Class<UmpDetailGetResponse> getResponseClass() {
		return UmpDetailGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(detailId,"detailId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
