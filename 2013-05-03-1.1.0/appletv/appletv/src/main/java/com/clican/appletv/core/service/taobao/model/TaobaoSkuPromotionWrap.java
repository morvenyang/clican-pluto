package com.clican.appletv.core.service.taobao.model;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class TaobaoSkuPromotionWrap {

	private TaobaoSkuPromotion defaultPromotion;
	
	private JSONObject jsonObject;

	private Map<String, TaobaoSkuPromotion> skuPromptionMap = new HashMap<String, TaobaoSkuPromotion>();
	
	private Long itemId;

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

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

}
