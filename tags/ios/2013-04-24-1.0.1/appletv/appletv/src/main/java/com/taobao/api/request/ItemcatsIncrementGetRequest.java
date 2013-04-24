package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.ItemcatsIncrementGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.itemcats.increment.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class ItemcatsIncrementGetRequest implements TaobaoRequest<ItemcatsIncrementGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 一级类目ID列表，用半角逗号(,)分隔，例如:"16,19562"，一次最多可以获取10个类目的增量数据。
	 */
	private String cids;

	/** 
	* 要获取最近哪一天的增量类目数据，可选值为：1~7。默认值为1，表示获取昨天发生变更的类目数据。
	 */
	private Long days;

	/** 
	* 获取类目的类型：1代表集市、2代表天猫
	 */
	private Long type;

	public void setCids(String cids) {
		this.cids = cids;
	}
	public String getCids() {
		return this.cids;
	}

	public void setDays(Long days) {
		this.days = days;
	}
	public Long getDays() {
		return this.days;
	}

	public void setType(Long type) {
		this.type = type;
	}
	public Long getType() {
		return this.type;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.itemcats.increment.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("cids", this.cids);
		txtParams.put("days", this.days);
		txtParams.put("type", this.type);
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

	public Class<ItemcatsIncrementGetResponse> getResponseClass() {
		return ItemcatsIncrementGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(cids,"cids");
		RequestCheckUtils.checkMaxListSize(cids,1000,"cids");
		RequestCheckUtils.checkMaxValue(days,7L,"days");
		RequestCheckUtils.checkMinValue(days,1L,"days");
		RequestCheckUtils.checkMaxValue(type,2L,"type");
		RequestCheckUtils.checkMinValue(type,1L,"type");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
