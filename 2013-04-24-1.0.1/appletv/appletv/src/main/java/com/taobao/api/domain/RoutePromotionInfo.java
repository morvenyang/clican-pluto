package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 线路的促销信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class RoutePromotionInfo extends TaobaoObject {

	private static final long serialVersionUID = 7786486999434321632L;

	/**
	 * 线路基本促销的id
	 */
	@ApiField("base_promotion_id")
	private String basePromotionId;

	/**
	 * 促销的描述
	 */
	@ApiField("promotion_description")
	private String promotionDescription;

	/**
	 * 具体活动详情页面链接
	 */
	@ApiField("promotion_url")
	private String promotionUrl;

	/**
	 * 是否包含非基础促销
	 */
	@ApiField("unbase_promotion")
	private Boolean unbasePromotion;

	public String getBasePromotionId() {
		return this.basePromotionId;
	}
	public void setBasePromotionId(String basePromotionId) {
		this.basePromotionId = basePromotionId;
	}

	public String getPromotionDescription() {
		return this.promotionDescription;
	}
	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public String getPromotionUrl() {
		return this.promotionUrl;
	}
	public void setPromotionUrl(String promotionUrl) {
		this.promotionUrl = promotionUrl;
	}

	public Boolean getUnbasePromotion() {
		return this.unbasePromotion;
	}
	public void setUnbasePromotion(Boolean unbasePromotion) {
		this.unbasePromotion = unbasePromotion;
	}

}
