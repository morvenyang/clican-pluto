package com.clican.appletv.core.service.taobao.model;

import java.util.List;

public class TaobaoFareRequestOrderItems {

	private String outOrderId;
	private String postMode;
	private boolean orderPostFree;
	private List<TaobaoFareRequestOrderItem> items;
	
	public String getOutOrderId() {
		return outOrderId;
	}
	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}
	public String getPostMode() {
		return postMode;
	}
	public void setPostMode(String postMode) {
		this.postMode = postMode;
	}
	public boolean isOrderPostFree() {
		return orderPostFree;
	}
	public void setOrderPostFree(boolean orderPostFree) {
		this.orderPostFree = orderPostFree;
	}
	public List<TaobaoFareRequestOrderItem> getItems() {
		return items;
	}
	public void setItems(List<TaobaoFareRequestOrderItem> items) {
		this.items = items;
	}
	
	
}
