package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TmallBrandcatSalesproGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: tmall.brandcat.salespro.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TmallBrandcatSalesproGetRequest implements TaobaoRequest<TmallBrandcatSalesproGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 被管控的品牌Id
	 */
	private Long brandId;

	/** 
	* 被管控的类目Id
	 */
	private Long catId;

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getBrandId() {
		return this.brandId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}
	public Long getCatId() {
		return this.catId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "tmall.brandcat.salespro.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("brand_id", this.brandId);
		txtParams.put("cat_id", this.catId);
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

	public Class<TmallBrandcatSalesproGetResponse> getResponseClass() {
		return TmallBrandcatSalesproGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(brandId,"brandId");
		RequestCheckUtils.checkNotEmpty(catId,"catId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
