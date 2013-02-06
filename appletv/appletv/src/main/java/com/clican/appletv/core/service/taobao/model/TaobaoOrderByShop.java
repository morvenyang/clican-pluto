package com.clican.appletv.core.service.taobao.model;

import java.util.List;

public class TaobaoOrderByShop {

	private String title;
	
	private String postMode;
	
	private String outOrderId;
	
	private String selectedFareId;
	
	private String fareName;

	private List<TaobaoFare> fareList;

	private List<TaobaoOrderByItem> itemList;
	
	private TaobaoPromotion promotion;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getPostMode() {
		return postMode;
	}

	public void setPostMode(String postMode) {
		this.postMode = postMode;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}

	public String getSelectedFareId() {
		return selectedFareId;
	}

	public void setSelectedFareId(String selectedFareId) {
		this.selectedFareId = selectedFareId;
	}

	public String getFareName() {
		return fareName;
	}

	public void setFareName(String fareName) {
		this.fareName = fareName;
	}

	public TaobaoPromotion getPromotion() {
		return promotion;
	}

	public void setPromotion(TaobaoPromotion promotion) {
		this.promotion = promotion;
	}
	
	
}
