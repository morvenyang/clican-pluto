package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UmpActivitiesListGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.ump.activities.list.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UmpActivitiesListGetRequest implements TaobaoRequest<UmpActivitiesListGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 营销活动id列表。
	 */
	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getIds() {
		return this.ids;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.ump.activities.list.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("ids", this.ids);
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

	public Class<UmpActivitiesListGetResponse> getResponseClass() {
		return UmpActivitiesListGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(ids,"ids");
		RequestCheckUtils.checkMaxListSize(ids,40,"ids");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
