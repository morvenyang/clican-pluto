package com.chinatelecom.xysq.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.chinatelecom.xysq.enumeration.InnerModule;
import com.chinatelecom.xysq.enumeration.NoticeCategory;

@Table(name = "ANNO_NOTICE")
@Entity
public class AnnouncementAndNotice {
	
	private Long id;
	
	private String title;
	
	private String content;
	
	private User submitter;
	
	private Date createTime;
	
	private Date modifyTime;
	
	private InnerModule innerModule;
	
	private Community community;
	
	private NoticeCategory noticeCategory;
	
	private List<AnnouncementAndNoticeContent> contents;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(length=4000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne
	@JoinColumn(name = "SUBMITTER_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public User getSubmitter() {
		return submitter;
	}

	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}

	@Column
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "INNER_MODULE")
	@Type(type = "com.chinatelecom.xysq.hibernate.EnumerationType", parameters = { @Parameter(name = "enumClass", value = "com.chinatelecom.xysq.enumeration.InnerModule") })
	public InnerModule getInnerModule() {
		return innerModule;
	}

	public void setInnerModule(InnerModule innerModule) {
		this.innerModule = innerModule;
	}

	@ManyToOne
	@JoinColumn(name = "COMMUNITY_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	@Column(name = "NOTICE_CATEGORY")
	@Type(type = "com.chinatelecom.xysq.hibernate.EnumerationType", parameters = { @Parameter(name = "enumClass", value = "com.chinatelecom.xysq.enumeration.NoticeCategory") })
	public NoticeCategory getNoticeCategory() {
		return noticeCategory;
	}

	public void setNoticeCategory(NoticeCategory noticeCategory) {
		this.noticeCategory = noticeCategory;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "announcementAndNotice", cascade = CascadeType.REMOVE)
	@OrderBy("displayIndex")
	public List<AnnouncementAndNoticeContent> getContents() {
		return contents;
	}

	public void setContents(List<AnnouncementAndNoticeContent> contents) {
		this.contents = contents;
	}
	
	

}
