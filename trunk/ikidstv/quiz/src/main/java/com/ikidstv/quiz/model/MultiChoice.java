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
	private String question;
	private String word1;
	private String word2;
	private String word3;
	private boolean answer1;
	private boolean answer2;
	private boolean answer3;
	
	
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
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	
}
