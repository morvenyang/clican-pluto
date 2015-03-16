package com.chinatelecom.xysq.json;

import java.util.Date;
import java.util.List;

import com.chinatelecom.xysq.enumeration.InnerModule;
import com.chinatelecom.xysq.enumeration.NoticeCategory;

public class AnnouncementAndNoticeJson {

	private Long id;

	private String title;

	private List<String> contents;

	private Date createTime;

	private Date modifyTime;

	private NoticeCategory noticeCategory;

	private InnerModule innerModule;

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

	public List<String> getContents() {
		return contents;
	}

	public void setContents(List<String> contents) {
		this.contents = contents;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public NoticeCategory getNoticeCategory() {
		return noticeCategory;
	}

	public void setNoticeCategory(NoticeCategory noticeCategory) {
		this.noticeCategory = noticeCategory;
	}

	public InnerModule getInnerModule() {
		return innerModule;
	}

	public void setInnerModule(InnerModule innerModule) {
		this.innerModule = innerModule;
	}

}
