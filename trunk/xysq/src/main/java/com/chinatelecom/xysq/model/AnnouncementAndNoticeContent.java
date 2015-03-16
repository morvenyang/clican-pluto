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
import org.hibernate.annotations.Type;

@Table(name = "ANNO_NOTICE_CONTENT")
@Entity
public class AnnouncementAndNoticeContent {
	
	private Long id;
	
	private String content;
	
	private Image image;
	
	private AnnouncementAndNotice announcementAndNotice;
	
	private int displayIndex;
	
	private boolean text;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length=4000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne
	@JoinColumn(name = "IMAGE_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@ManyToOne
	@JoinColumn(name = "ANNO_NOTICE_ID", nullable = false)
	@Fetch(FetchMode.JOIN)
	public AnnouncementAndNotice getAnnouncementAndNotice() {
		return announcementAndNotice;
	}

	public void setAnnouncementAndNotice(AnnouncementAndNotice announcementAndNotice) {
		this.announcementAndNotice = announcementAndNotice;
	}

	@Column(name="DISPLAY_INDEX")
	public int getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(int displayIndex) {
		this.displayIndex = displayIndex;
	}

	@Column(name = "GLOBAL")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isText() {
		return text;
	}

	public void setText(boolean text) {
		this.text = text;
	}
	
	

}
