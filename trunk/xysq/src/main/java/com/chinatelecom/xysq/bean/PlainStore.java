package com.chinatelecom.xysq.bean;

public class PlainStore {

	private Long id;
	
	private String name;
	
	private String pinyin;
	
	private String shortPinyin;
	
	public PlainStore(Long id, String name, String pinyin, String shortPinyin) {
		this.id = id;
		this.name = name;
		this.pinyin = pinyin;
		this.shortPinyin = shortPinyin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getShortPinyin() {
		return shortPinyin;
	}

	public void setShortPinyin(String shortPinyin) {
		this.shortPinyin = shortPinyin;
	}

	
	
	
}
