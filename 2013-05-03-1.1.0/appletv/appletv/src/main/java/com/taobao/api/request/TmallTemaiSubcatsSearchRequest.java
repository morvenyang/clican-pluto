package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TmallTemaiSubcatsSearchResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: tmall.temai.subcats.search request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TmallTemaiSubcatsSearchRequest implements TaobaoRequest<TmallTemaiSubcatsSearchResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 父类目ID，固定是特卖前台一级类目id：50100982
	 */
	private Long cat;

	public void setCat(Long cat) {
		this.cat = cat;
	}
	public Long getCat() {
		return this.cat;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "tmall.temai.subcats.search";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("cat", this.cat);
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

	public Class<TmallTemaiSubcatsSearchResponse> getResponseClass() {
		return TmallTemaiSubcatsSearchResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(cat,"cat");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
