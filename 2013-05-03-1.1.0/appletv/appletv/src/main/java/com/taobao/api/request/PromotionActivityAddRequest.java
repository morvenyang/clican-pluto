package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.PromotionActivityAddResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.promotion.activity.add request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class PromotionActivityAddRequest implements TaobaoRequest<PromotionActivityAddResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 优惠券总领用数量
	 */
	private Long couponCount;

	/** 
	* 优惠券的id，唯一标识这个优惠券
	 */
	private Long couponId;

	/** 
	* 每个人最多领用数量，0代表不限
	 */
	private Long personLimitCount;

	/** 
	* 是否将该活动优惠券同步到淘券市场
1（不同步）
2（同步）
	 */
	private Long uploadToTaoquan;

	public void setCouponCount(Long couponCount) {
		this.couponCount = couponCount;
	}
	public Long getCouponCount() {
		return this.couponCount;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public Long getCouponId() {
		return this.couponId;
	}

	public void setPersonLimitCount(Long personLimitCount) {
		this.personLimitCount = personLimitCount;
	}
	public Long getPersonLimitCount() {
		return this.personLimitCount;
	}

	public void setUploadToTaoquan(Long uploadToTaoquan) {
		this.uploadToTaoquan = uploadToTaoquan;
	}
	public Long getUploadToTaoquan() {
		return this.uploadToTaoquan;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.promotion.activity.add";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("coupon_count", this.couponCount);
		txtParams.put("coupon_id", this.couponId);
		txtParams.put("person_limit_count", this.personLimitCount);
		txtParams.put("upload_to_taoquan", this.uploadToTaoquan);
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

	public Class<PromotionActivityAddResponse> getResponseClass() {
		return PromotionActivityAddResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(couponCount,"couponCount");
		RequestCheckUtils.checkMaxValue(couponCount,999999L,"couponCount");
		RequestCheckUtils.checkMinValue(couponCount,1L,"couponCount");
		RequestCheckUtils.checkNotEmpty(couponId,"couponId");
		RequestCheckUtils.checkNotEmpty(personLimitCount,"personLimitCount");
		RequestCheckUtils.checkMaxValue(personLimitCount,5L,"personLimitCount");
		RequestCheckUtils.checkMinValue(personLimitCount,0L,"personLimitCount");
		RequestCheckUtils.checkMaxValue(uploadToTaoquan,2L,"uploadToTaoquan");
		RequestCheckUtils.checkMinValue(uploadToTaoquan,1L,"uploadToTaoquan");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
