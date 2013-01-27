package com.clican.appletv.core.model;

import java.util.List;

public class TaobaoCategory {

	private Long id;
	private String title;
	private String subTitle;
	private boolean hasChild;
	private String picUrl;
	private List<TaobaoCategory> children;
	private Long[] childrenCids;

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

	public Long[] getChildrenCids() {
		return childrenCids;
	}

	public void setChildrenCids(Long[] childrenCids) {
		this.childrenCids = childrenCids;
	}

	public static void toString(List<TaobaoCategory> list, StringBuffer sb,
			String changeLine) {
		for (TaobaoCategory tc : list) {
			sb.append(changeLine);
			sb.append(tc.getId()).append(":").append(tc.getTitle());
			if (tc.getChildren() != null && tc.getChildren().size() > 0) {
				toString(tc.getChildren(), sb, changeLine + "\t");
			}
		}
	}

}
