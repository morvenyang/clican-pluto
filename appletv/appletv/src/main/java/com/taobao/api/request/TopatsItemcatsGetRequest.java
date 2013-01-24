package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TopatsItemcatsGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.topats.itemcats.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TopatsItemcatsGetRequest implements TaobaoRequest<TopatsItemcatsGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 一级类目ID列表（非一级类目将会被忽略），用半角逗号(,)分隔，例如:"16,19562"，一次最多可以获取10个类目的增量数据。<span style="color:red">注：传入0代表获取所有类目的数据,默认获取所有类目数据</span>
	 */
	private String cids;

	/** 
	* 类目数据输出格式，可选值为：csv, json（默认csv格式返回）
	 */
	private String outputFormat;

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

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
	public String getOutputFormat() {
		return this.outputFormat;
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
		return "taobao.topats.itemcats.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("cids", this.cids);
		txtParams.put("output_format", this.outputFormat);
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

	public Class<TopatsItemcatsGetResponse> getResponseClass() {
		return TopatsItemcatsGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkMaxValue(type,2L,"type");
		RequestCheckUtils.checkMinValue(type,1L,"type");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
