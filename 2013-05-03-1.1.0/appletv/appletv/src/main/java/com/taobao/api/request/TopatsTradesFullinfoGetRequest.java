package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TopatsTradesFullinfoGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.topats.trades.fullinfo.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TopatsTradesFullinfoGetRequest implements TaobaoRequest<TopatsTradesFullinfoGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 可以返回taobao.trade.fullinfo.get允许的所有字段。
	 */
	private String fields;

	/** 
	* 交易订单号tid列表，多个tid之间用半角分号分隔。tid个数的取值范围是：1~100个。由于这个接口限制每个应用的调用量是3万次/天，所以强烈建议采用尽可能多的tid，以取到更多的交易数据。
	 */
	private String tids;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setTids(String tids) {
		this.tids = tids;
	}
	public String getTids() {
		return this.tids;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.topats.trades.fullinfo.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("tids", this.tids);
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

	public Class<TopatsTradesFullinfoGetResponse> getResponseClass() {
		return TopatsTradesFullinfoGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(fields,"fields");
		RequestCheckUtils.checkNotEmpty(tids,"tids");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
