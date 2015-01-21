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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Table(name = "FORUM_TOPIC")
@Entity
public class ForumTopic {

	private Long id;

	private User submitter;

	private String title;

	private Date createTime;

	private Date modifyTime;

	private String content;

	private List<Image> images;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Column(length = 4000)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@Lob
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "store", cascade = CascadeType.REMOVE)
	@OrderBy("seq")
	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

}
