package com.clican.appletv.core.service.taobao.model;

import java.util.List;
import java.util.Map;

public class TaobaoConfirmOrder {

	private Long selectedAddrId;

	private List<TaobaoAddress> addrList;

	private List<TaobaoOrderByShop> shopList;

	private Map<String, String> forms;
	
	private byte[] confirmOrderImage;
	
	private Double total;
	
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

	public Long getSelectedAddrId() {
		return selectedAddrId;
	}

	public void setSelectedAddrId(Long selectedAddrId) {
		this.selectedAddrId = selectedAddrId;
	}

	public Map<String, String> getForms() {
		return forms;
	}

	public void setForms(Map<String, String> forms) {
		this.forms = forms;
	}

	public byte[] getConfirmOrderImage() {
		return confirmOrderImage;
	}

	public void setConfirmOrderImage(byte[] confirmOrderImage) {
		this.confirmOrderImage = confirmOrderImage;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
