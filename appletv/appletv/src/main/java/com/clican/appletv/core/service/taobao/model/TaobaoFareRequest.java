package com.clican.appletv.core.service.taobao.model;

import java.util.List;

public class TaobaoFareRequest {

	private String secondDivisionId;
	private String activity_id;
	private String useSelfCarry;
	private String buyer_from;
	private String shop_id;
	private String use_cod;
	private String channel;
	
	private List<TaobaoFareRequestOrderItems> orderItems;

	public String getSecondDivisionId() {
		return secondDivisionId;
	}

	public void setSecondDivisionId(String secondDivisionId) {
		this.secondDivisionId = secondDivisionId;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getUseSelfCarry() {
		return useSelfCarry;
	}

	public void setUseSelfCarry(String useSelfCarry) {
		this.useSelfCarry = useSelfCarry;
	}

	public String getBuyer_from() {
		return buyer_from;
	}

	public void setBuyer_from(String buyer_from) {
		this.buyer_from = buyer_from;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getUse_cod() {
		return use_cod;
	}

	public void setUse_cod(String use_cod) {
		this.use_cod = use_cod;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public List<TaobaoFareRequestOrderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TaobaoFareRequestOrderItems> orderItems) {
		this.orderItems = orderItems;
	}
	
	
	
}
