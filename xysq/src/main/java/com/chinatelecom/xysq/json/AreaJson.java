package com.chinatelecom.xysq.json;

import java.util.List;

public class AreaJson {

	private Long id;

	private String name;

	private String pinyin;

	private String shortPinyin;
	
	private List<AreaJson> areas;
	
	private List<CommunityJson> communities;
	
	private String fullName;

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

	public List<AreaJson> getAreas() {
		return areas;
	}

	public void setAreas(List<AreaJson> areas) {
		this.areas = areas;
	}

	public List<CommunityJson> getCommunities() {
		return communities;
	}

	public void setCommunities(List<CommunityJson> communities) {
		this.communities = communities;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
}
