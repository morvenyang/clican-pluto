package com.clican.appletv.core.service.taobao.model;

import java.util.List;

public class TaobaoConfirmOrder {

	private List<TaobaoAddress> addrList;
	
	private List<TaobaoOrderByShop> shopList;

	public List<TaobaoAddress> getAddrList() {
		return addrList;
	}

	public void setAddrList(List<TaobaoAddress> addrList) {
		this.addrList = addrList;
	}

	public List<TaobaoOrderByShop> getShopList() {
		return shopList;
	}

	public void setShopList(List<TaobaoOrderByShop> shopList) {
		this.shopList = shopList;
	}
	
	
}
