package com.ikidstv.quiz.model;

import java.util.Date;
import java.util.Set;

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
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Table(name = "ALL_QUIZ")
@Entity
public class Quiz {

	private Long id;
	private String title;
	private String seasonId;
	private String season;
	private String episodeId;
	private String episode;
	private Template template;
	private Long metadataId;
	// 1. 未提交 2. 审核中 3. 发布 4. 审核不通过 5. 废弃
	private int status = 1;
	private boolean recording;
	private User user;
	private User auditUser;
	private Integer difficulty;
	private Integer age;
	private boolean iphone;
	private boolean ipad;
	private Date createTime;
	private Date publishTime;
	private String backgroundImage;
	private String frontImage;
	private boolean placementTest;
	
	
	private Set<QuizLearningPointRel> learningPointRelSet;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "SEASON_ID")
	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	@Column(name = "EPISODE_ID")
	public String getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	@Column(name = "SEASON")
	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	@Column(name = "EPISODE")
	public String getEpisode() {
		return episode;
	}

	public void setEpisode(String episode) {
		this.episode = episode;
	}

	@ManyToOne
	@JoinColumn(name = "TEMPLATE_ID")
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	@Column(name = "METADATA_ID")
	public Long getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}

	@Column(name = "STATUS")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "RECORDING")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	@ManyToOne
	@JoinColumn(name="USER_ID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name="AUDITOR_ID")
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	@Column(name="DIFFICULTY")
	public Integer getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	@Column(name="AGE")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name = "IPHONE")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isIphone() {
		return iphone;
	}

	public void setIphone(boolean iphone) {
		this.iphone = iphone;
	}

	@Column(name = "IPAD")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isIpad() {
		return ipad;
	}

	public void setIpad(boolean ipad) {
		this.ipad = ipad;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "PUBLISH_TIME")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="quiz",cascade=CascadeType.ALL)
	public Set<QuizLearningPointRel> getLearningPointRelSet() {
		return learningPointRelSet;
	}

	public void setLearningPointRelSet(Set<QuizLearningPointRel> learningPointRelSet) {
		this.learningPointRelSet = learningPointRelSet;
	}

	@Column(name = "BACKGROUND_IMAGE")
	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	@Column(name = "FRONT_IMAGE")
	public String getFrontImage() {
		return frontImage;
	}

	public void setFrontImage(String frontImage) {
		this.frontImage = frontImage;
	}

	@Column(name = "PLACEMENT_TEST")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isPlacementTest() {
		return placementTest;
	}

	public void setPlacementTest(boolean placementTest) {
		this.placementTest = placementTest;
	}

	
	
	
}
