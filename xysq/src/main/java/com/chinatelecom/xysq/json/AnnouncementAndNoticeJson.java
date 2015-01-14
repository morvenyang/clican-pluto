package com.chinatelecom.xysq.json;

import java.util.Date;

import com.chinatelecom.xysq.enumeration.NoticeCategory;

public class AnnouncementAndNoticeJson {

	private Long id;

	private String title;

	private String content;

	private Date createTime;

	private Date modifyTime;

	private NoticeCategory noticeCategory;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}
