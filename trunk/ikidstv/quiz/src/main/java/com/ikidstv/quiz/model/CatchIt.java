package com.ikidstv.quiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "CATCH_IT")
@Entity
public class CatchIt extends Metadata {

	private Long id;
	
	private String title;
	private String titleRecord;
	private String word1;
	private String word2;
	private String word3;
	private String word4;
	private String word5;
	private String word6;
	private String word7;
	private String word8;
	private String word9;
	private String word10;
	private String record1;
	private String record2;
	private String record3;
	private String record4;
	private String record5;
	private String record6;
	private String record7;
	private String record8;
	private String record9;
	private String record10;
	
	
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
	public String getWord6() {
		return word6;
	}
	public void setWord6(String word6) {
		this.word6 = word6;
	}
	@Column
	public String getWord7() {
		return word7;
	}
	public void setWord7(String word7) {
		this.word7 = word7;
	}
	@Column
	public String getWord8() {
		return word8;
	}
	public void setWord8(String word8) {
		this.word8 = word8;
	}
	@Column
	public String getWord9() {
		return word9;
	}
	public void setWord9(String word9) {
		this.word9 = word9;
	}
	@Column
	public String getWord10() {
		return word10;
	}
	public void setWord10(String word10) {
		this.word10 = word10;
	}
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column
	public String getTitleRecord() {
		return titleRecord;
	}
	public void setTitleRecord(String titleRecord) {
		this.titleRecord = titleRecord;
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
	@Column
	public String getRecord6() {
		return record6;
	}
	public void setRecord6(String record6) {
		this.record6 = record6;
	}
	@Column
	public String getRecord7() {
		return record7;
	}
	public void setRecord7(String record7) {
		this.record7 = record7;
	}
	@Column
	public String getRecord8() {
		return record8;
	}
	public void setRecord8(String record8) {
		this.record8 = record8;
	}
	@Column
	public String getRecord9() {
		return record9;
	}
	public void setRecord9(String record9) {
		this.record9 = record9;
	}
	@Column
	public String getRecord10() {
		return record10;
	}
	public void setRecord10(String record10) {
		this.record10 = record10;
	}
	
	

}
