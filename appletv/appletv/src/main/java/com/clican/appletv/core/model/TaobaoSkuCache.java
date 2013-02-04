package com.clican.appletv.core.model;

import java.util.List;

import com.taobao.api.domain.Item;

public class TaobaoSkuCache {

	private TaobaoSkuCategory category;
	
	private Item item;

	public TaobaoSkuCategory getCategory() {
		return category;
	}

	public void setCategory(TaobaoSkuCategory category) {
		this.category = category;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}
