package com.clican.appletv.core.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.api.domain.Item;

public class TaobaoSkuCache {

	private Item item;

	private List<String> skuLabelList;

	private Map<String, Set<TaobaoSkuValue>> skuLabelValueMap;
	
	private Map<String,Object> skuRelationMap;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<String> getSkuLabelList() {
		return skuLabelList;
	}

	public void setSkuLabelList(List<String> skuLabelList) {
		this.skuLabelList = skuLabelList;
	}

	public Map<String, Set<TaobaoSkuValue>> getSkuLabelValueMap() {
		return skuLabelValueMap;
	}

	public void setSkuLabelValueMap(
			Map<String, Set<TaobaoSkuValue>> skuLabelValueMap) {
		this.skuLabelValueMap = skuLabelValueMap;
	}

	public Map<String, Object> getSkuRelationMap() {
		return skuRelationMap;
	}

	public void setSkuRelationMap(Map<String, Object> skuRelationMap) {
		this.skuRelationMap = skuRelationMap;
	}

	

	
	
	
	

}
