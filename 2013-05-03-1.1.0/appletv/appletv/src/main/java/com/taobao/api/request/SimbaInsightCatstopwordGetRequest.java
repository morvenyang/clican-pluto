package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SimbaInsightCatstopwordGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.simba.insight.catstopword.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SimbaInsightCatstopwordGetRequest implements TaobaoRequest<SimbaInsightCatstopwordGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 类目id数组，最大长度200
	 */
	private String categoryIds;

	/** 
	* 主人昵称
	 */
	private String nick;

	/** 
	* 最大返回数量(1-100)
	 */
	private Long resultNum;

	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}
	public String getCategoryIds() {
		return this.categoryIds;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
	}

	public void setResultNum(Long resultNum) {
		this.resultNum = resultNum;
	}
	public Long getResultNum() {
		return this.resultNum;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.simba.insight.catstopword.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("category_ids", this.categoryIds);
		txtParams.put("nick", this.nick);
		txtParams.put("result_num", this.resultNum);
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

	public Class<SimbaInsightCatstopwordGetResponse> getResponseClass() {
		return SimbaInsightCatstopwordGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(categoryIds,"categoryIds");
		RequestCheckUtils.checkMaxListSize(categoryIds,200,"categoryIds");
		RequestCheckUtils.checkNotEmpty(resultNum,"resultNum");
		RequestCheckUtils.checkMaxValue(resultNum,100L,"resultNum");
		RequestCheckUtils.checkMinValue(resultNum,1L,"resultNum");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
