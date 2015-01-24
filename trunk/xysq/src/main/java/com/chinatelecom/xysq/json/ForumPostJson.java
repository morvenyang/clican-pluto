package com.chinatelecom.xysq.json;

import java.util.Date;
import java.util.List;

public class ForumPostJson {

	private Long id;

	private UserJson submitter;

	private Date createTime;

	private Date modifyTime;

	private String content;
	
	private String replyContent;

	private List<String> images;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserJson getSubmitter() {
		return submitter;
	}

	public void setSubmitter(UserJson submitter) {
		this.submitter = submitter;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	
	
}
