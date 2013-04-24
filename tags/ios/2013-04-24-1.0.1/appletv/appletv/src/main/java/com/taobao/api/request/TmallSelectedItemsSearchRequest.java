package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TmallSelectedItemsSearchResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: tmall.selected.items.search request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TmallSelectedItemsSearchRequest implements TaobaoRequest<TmallSelectedItemsSearchResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 后台类目ID，支持父类目或叶子类目，可以通过taobao.itemcats.get 获取到后台类目ID列表
	 */
	private Long cid;

	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getCid() {
		return this.cid;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "tmall.selected.items.search";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("cid", this.cid);
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

	public Class<TmallSelectedItemsSearchResponse> getResponseClass() {
		return TmallSelectedItemsSearchResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(cid,"cid");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
