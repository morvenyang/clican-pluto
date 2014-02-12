package com.ikidstv.quiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Table(name = "MULTI_CHOICE")
@Entity
public class MultiChoice implements Metadata{

	private Long id;
	private String title;
	private String titleRecord;
	private String titlePicture;
	private String word1;
	private String word2;
	private String word3;
	private boolean answer1;
	private boolean answer2;
	private boolean answer3;
	private String record1;
	private String record2;
	private String record3;
	private String picture1;
	private String picture2;
	private String picture3;
	
	
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
	public String getWord1() {
		return word1;
	}
	public void setWord1(String word1) {
		this.word1 = word1;
	}
	@Column
	public String getWord2() {
		return word2;
	}
	public void setWord2(String word2) {
		this.word2 = word2;
	}
	@Column
	public String getWord3() {
		return word3;
	}
	public void setWord3(String word3) {
		this.word3 = word3;
	}
	@Column
	@Type(type="yes_no")
	public boolean isAnswer1() {
		return answer1;
	}
	public void setAnswer1(boolean answer1) {
		this.answer1 = answer1;
	}
	@Column
	@Type(type="yes_no")
	public boolean isAnswer2() {
		return answer2;
	}
	public void setAnswer2(boolean answer2) {
		this.answer2 = answer2;
	}
	@Column
	@Type(type="yes_no")
	public boolean isAnswer3() {
		return answer3;
	}
	public void setAnswer3(boolean answer3) {
		this.answer3 = answer3;
	}
	@Column(length=500)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column
	public String getRecord1() {
		return record1;
	}
	public void setRecord1(String record1) {
		this.record1 = record1;
	}
	@Column
	public String getRecord2() {
		return record2;
	}
	public void setRecord2(String record2) {
		this.record2 = record2;
	}
	@Column
	public String getRecord3() {
		return record3;
	}
	public void setRecord3(String record3) {
		this.record3 = record3;
	}
	@Column
	public String getTitleRecord() {
		return titleRecord;
	}
	public void setTitleRecord(String titleRecord) {
		this.titleRecord = titleRecord;
	}
	@Column
	public String getTitlePicture() {
		return titlePicture;
	}
	public void setTitlePicture(String titlePicture) {
		this.titlePicture = titlePicture;
	}
	@Column
	public String getPicture1() {
		return picture1;
	}
	public void setPicture1(String picture1) {
		this.picture1 = picture1;
	}
	@Column
	public String getPicture2() {
		return picture2;
	}
	public void setPicture2(String picture2) {
		this.picture2 = picture2;
	}
	@Column
	public String getPicture3() {
		return picture3;
	}
	public void setPicture3(String picture3) {
		this.picture3 = picture3;
	}
	
	
	
	
}
