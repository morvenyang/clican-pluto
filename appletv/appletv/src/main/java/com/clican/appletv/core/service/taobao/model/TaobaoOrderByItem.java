package com.clican.appletv.core.service.taobao.model;

public class TaobaoOrderByItem {

	private Long itemId;
	
	private String title;
	
	private String picUrl;
	
	private String dataId;
	
	private Float price;
	
	private Integer quantity; 
	
	private TaobaoPromotion promotion;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public TaobaoPromotion getPromotion() {
		return promotion;
	}

	public void setPromotion(TaobaoPromotion promotion) {
		this.promotion = promotion;
	}

	public Double getActualPrice(){
		if(promotion==null){
			return price.doubleValue()*quantity;
		}else{
			return price*quantity-promotion.getDiscount();
		}
	}
	
	
}
