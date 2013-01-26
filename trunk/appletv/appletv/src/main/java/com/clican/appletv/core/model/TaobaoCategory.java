package com.clican.appletv.core.model;

import java.util.List;

public class TaobaoCategory {

	private Long id;
	private String title;
	private String subTitle;
	private boolean hasChild;
	private String picUrl;
	private List<TaobaoCategory> children;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public List<TaobaoCategory> getChildren() {
		return children;
	}
	public void setChildren(List<TaobaoCategory> children) {
		this.children = children;
	}
	
	
	
}
