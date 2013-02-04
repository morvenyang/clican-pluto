package com.clican.appletv.core.model;

import java.util.Map;

import com.taobao.api.domain.Sku;


public class TaobaoSku {

	private Sku sku;
	
	private String skuValue;
	
	private String skuLabel;
	
	private Map<String,TaobaoSkuCategory> categoryMap;

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Map<String, TaobaoSkuCategory> getCategoryMap() {
		return categoryMap;
	}

	public void setCategoryMap(Map<String, TaobaoSkuCategory> categoryMap) {
		this.categoryMap = categoryMap;
	}

	public String getSkuValue() {
		return skuValue;
	}

	public void setSkuValue(String skuValue) {
		this.skuValue = skuValue;
	}

	public String getSkuLabel() {
		return skuLabel;
	}

	public void setSkuLabel(String skuLabel) {
		this.skuLabel = skuLabel;
	}
	
	
	
}
