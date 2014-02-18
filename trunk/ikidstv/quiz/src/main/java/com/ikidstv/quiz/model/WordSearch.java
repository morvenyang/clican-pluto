package com.ikidstv.quiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "WORD_SEARCH")
@Entity
public class WordSearch extends Metadata{

	
	private Long id;
	private String word1;
	private String word2;
	private String word3;
	private String word4;
	private String word5;
	
	private String record1;
	private String record2;
	private String record3;
	private String record4;
	private String record5;
	
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
	public String getWord4() {
		return word4;
	}
	public void setWord4(String word4) {
		this.word4 = word4;
	}
	@Column
	public String getWord5() {
		return word5;
	}
	public void setWord5(String word5) {
		this.word5 = word5;
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
	public String getRecord4() {
		return record4;
	}
	public void setRecord4(String record4) {
		this.record4 = record4;
	}
	@Column
	public String getRecord5() {
		return record5;
	}
	public void setRecord5(String record5) {
		this.record5 = record5;
	}
	
	
	
}
