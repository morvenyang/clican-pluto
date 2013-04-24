package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.PromotionLimitdiscountDetailGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.promotion.limitdiscount.detail.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class PromotionLimitdiscountDetailGetRequest implements TaobaoRequest<PromotionLimitdiscountDetailGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 限时打折ID。这个针对查询唯一限时打折情况。
	 */
	private Long limitDiscountId;

	public void setLimitDiscountId(Long limitDiscountId) {
		this.limitDiscountId = limitDiscountId;
	}
	public Long getLimitDiscountId() {
		return this.limitDiscountId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.promotion.limitdiscount.detail.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("limit_discount_id", this.limitDiscountId);
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

	public Class<PromotionLimitdiscountDetailGetResponse> getResponseClass() {
		return PromotionLimitdiscountDetailGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(limitDiscountId,"limitDiscountId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
