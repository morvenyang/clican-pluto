package com.ikidstv.quiz.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "MATCHING")
@Entity
public class Matching extends Metadata {

	private Long id;
	private String picture1;
	private String word1;
	private String picture2;
	private String word2;
	private String picture3;
	private String word3;
	private String picture4;
	private String word4;

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
	public String getPicture1() {
		return picture1;
	}

	public void setPicture1(String picture1) {
		this.picture1 = picture1;
	}

	@Column
	public String getWord1() {
		return word1;
	}

	public void setWord1(String word1) {
		this.word1 = word1;
	}



	@Column
	public String getPicture2() {
		return picture2;
	}

	public void setPicture2(String picture2) {
		this.picture2 = picture2;
	}

	@Column
	public String getWord2() {
		return word2;
	}

	public void setWord2(String word2) {
		this.word2 = word2;
	}



	@Column
	public String getPicture3() {
		return picture3;
	}

	public void setPicture3(String picture3) {
		this.picture3 = picture3;
	}

	@Column
	public String getWord3() {
		return word3;
	}

	public void setWord3(String word3) {
		this.word3 = word3;
	}



	@Column
	public String getPicture4() {
		return picture4;
	}

	public void setPicture4(String picture4) {
		this.picture4 = picture4;
	}

	@Column
	public String getWord4() {
		return word4;
	}

	public void setWord4(String word4) {
		this.word4 = word4;
	}



	@Transient
	public List<String> getPictures() {
		List<String> pictures = new ArrayList<String>();
		if(picture1!=null){
			pictures.add(picture1);
		}
		if(picture2!=null){
			pictures.add(picture2);
		}
		if(picture3!=null){
			pictures.add(picture3);
		}
		if(picture4!=null){
			pictures.add(picture4);
		}
		return pictures;
	}

	@Transient
	public List<String> getWords() {
		List<String> words = new ArrayList<String>();
		words.add(word1);
		words.add(word2);
		words.add(word3);
		words.add(word4);
		return words;
	}
}
