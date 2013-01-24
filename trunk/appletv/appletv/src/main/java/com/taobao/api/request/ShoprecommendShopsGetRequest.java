package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.ShoprecommendShopsGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.shoprecommend.shops.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class ShoprecommendShopsGetRequest implements TaobaoRequest<ShoprecommendShopsGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 请求个数，建议获取16个
	 */
	private Long count;

	/** 
	* 额外参数
	 */
	private String ext;

	/** 
	* 请求类型，1：关联店铺推荐。其他值当非法值处理
	 */
	private Long recommendType;

	/** 
	* 传入卖家ID
	 */
	private Long sellerId;

	public void setCount(Long count) {
		this.count = count;
	}
	public Long getCount() {
		return this.count;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getExt() {
		return this.ext;
	}

	public void setRecommendType(Long recommendType) {
		this.recommendType = recommendType;
	}
	public Long getRecommendType() {
		return this.recommendType;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getSellerId() {
		return this.sellerId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.shoprecommend.shops.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("count", this.count);
		txtParams.put("ext", this.ext);
		txtParams.put("recommend_type", this.recommendType);
		txtParams.put("seller_id", this.sellerId);
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

	public Class<ShoprecommendShopsGetResponse> getResponseClass() {
		return ShoprecommendShopsGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(count,"count");
		RequestCheckUtils.checkNotEmpty(recommendType,"recommendType");
		RequestCheckUtils.checkNotEmpty(sellerId,"sellerId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
