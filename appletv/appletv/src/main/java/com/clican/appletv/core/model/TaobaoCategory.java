package com.clican.appletv.core.model;

import java.util.ArrayList;
import java.util.List;

public class TaobaoCategory {

	private Long id;
	private Long parentId=0L;
	private String title;
	private String base64Title;
	private String subTitle;
	private boolean hasChild;
	private String picUrl;
	private List<TaobaoCategory> children = new ArrayList<TaobaoCategory>();
	private String[] childrenCids;
	

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

	

	public String[] getChildrenCids() {
		return childrenCids;
	}

	public void setChildrenCids(String[] childrenCids) {
		this.childrenCids = childrenCids;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getBase64Title() {
		return base64Title;
	}

	public void setBase64Title(String base64Title) {
		this.base64Title = base64Title;
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
