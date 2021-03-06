package com.chinatelecom.xysq.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Table(name = "IMAGE")
@Entity
public class Image {

	private Long id;

	private String name;

	private String path;

	private String thumbnailPath;

	private int seq;

	private Store store;

	private ForumTopic forumTopic;

	private ForumPost forumPost;

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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column
	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	@Column
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	@ManyToOne
	@JoinColumn(name = "STORE_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@ManyToOne
	@JoinColumn(name = "FORUM_TOPIC_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public ForumTopic getForumTopic() {
		return forumTopic;
	}

	public void setForumTopic(ForumTopic forumTopic) {
		this.forumTopic = forumTopic;
	}

	@ManyToOne
	@JoinColumn(name = "FORUM_POST_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public ForumPost getForumPost() {
		return forumPost;
	}

	public void setForumPost(ForumPost forumPost) {
		this.forumPost = forumPost;
	}

}
