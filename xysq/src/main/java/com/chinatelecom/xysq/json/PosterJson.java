package com.chinatelecom.xysq.json;

import com.chinatelecom.xysq.enumeration.InnerModule;
import com.chinatelecom.xysq.enumeration.PosterType;

public class PosterJson {

	private Long id;
	
	private String name;
	
	private String imagePath;
	
	private PosterType type;
	
	private String html5Link;
	
	private InnerModule innerModule;
	
	private Long storeId;

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

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public PosterType getType() {
		return type;
	}

	public void setType(PosterType type) {
		this.type = type;
	}

	public String getHtml5Link() {
		return html5Link;
	}

	public void setHtml5Link(String html5Link) {
		this.html5Link = html5Link;
	}

	public InnerModule getInnerModule() {
		return innerModule;
	}

	public void setInnerModule(InnerModule innerModule) {
		this.innerModule = innerModule;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
	
	
	
}
