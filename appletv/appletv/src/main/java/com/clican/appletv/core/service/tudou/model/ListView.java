package com.clican.appletv.core.service.tudou.model;

import java.io.Serializable;

public class ListView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1887153239361214828L;
	
	private Long itemid;
	private String picurl;
	private String title;
	private Integer isalbum;
	
	public Long getItemid() {
		return itemid;
	}
	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getIsalbum() {
		return isalbum;
	}
	public void setIsalbum(Integer isalbum) {
		this.isalbum = isalbum;
	}
	
	
}
