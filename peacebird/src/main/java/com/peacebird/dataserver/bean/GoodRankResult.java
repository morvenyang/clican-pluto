package com.peacebird.dataserver.bean;

public class GoodRankResult {

	private String name;
	private Long amount;
	private Integer count;
	private String imageLink;
	private String imageLinkMin;
	
	
	public GoodRankResult(String name, Long amount, Integer count,
			String imageLink, String imageLinkMin) {
		this.name = name;
		this.amount = amount;
		this.count = count;
		this.imageLink = imageLink;
		this.imageLinkMin = imageLinkMin;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public String getImageLinkMin() {
		return imageLinkMin;
	}
	public void setImageLinkMin(String imageLinkMin) {
		this.imageLinkMin = imageLinkMin;
	}
	
	

	
	
}
