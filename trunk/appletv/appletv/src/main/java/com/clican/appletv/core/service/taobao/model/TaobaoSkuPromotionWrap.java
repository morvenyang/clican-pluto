package com.clican.appletv.core.service.taobao.model;

import java.util.HashMap;
import java.util.Map;

public class TaobaoSkuPromotionWrap {

	private TaobaoSkuPromotion defaultPromotion;

	private Map<String, TaobaoSkuPromotion> skuPromptionMap = new HashMap<String, TaobaoSkuPromotion>();

	public TaobaoSkuPromotion getDefaultPromotion() {
		return defaultPromotion;
	}

	public void setDefaultPromotion(TaobaoSkuPromotion defaultPromotion) {
		this.defaultPromotion = defaultPromotion;
	}

	public Map<String, TaobaoSkuPromotion> getSkuPromptionMap() {
		return skuPromptionMap;
	}

	public void setSkuPromptionMap(
			Map<String, TaobaoSkuPromotion> skuPromptionMap) {
		this.skuPromptionMap = skuPromptionMap;
	}

}
