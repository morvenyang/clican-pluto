package com.clican.appletv.core.service.taobao.model;

import java.util.List;

public class TaobaoOrderByShop {

	private String shop;

	private List<TaobaoFare> fareList;

	private List<TaobaoOrderByItem> itemList;

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public List<TaobaoFare> getFareList() {
		return fareList;
	}

	public void setFareList(List<TaobaoFare> fareList) {
		this.fareList = fareList;
	}

	public List<TaobaoOrderByItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<TaobaoOrderByItem> itemList) {
		this.itemList = itemList;
	}
	
	
}
