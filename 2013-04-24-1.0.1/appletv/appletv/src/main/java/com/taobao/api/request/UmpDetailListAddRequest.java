package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UmpDetailListAddResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.ump.detail.list.add request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UmpDetailListAddRequest implements TaobaoRequest<UmpDetailListAddResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 营销活动id。
	 */
	private Long actId;

	/** 
	* 营销详情的列表。此列表由detail的json字符串组成。最多插入为10个。
	 */
	private String details;

	public void setActId(Long actId) {
		this.actId = actId;
	}
	public Long getActId() {
		return this.actId;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	public String getDetails() {
		return this.details;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.ump.detail.list.add";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("act_id", this.actId);
		txtParams.put("details", this.details);
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

	public Class<UmpDetailListAddResponse> getResponseClass() {
		return UmpDetailListAddResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(actId,"actId");
		RequestCheckUtils.checkNotEmpty(details,"details");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
