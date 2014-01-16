package com.ikidstv.quiz.bean;

public class SpringProperty {

	private String version;
	private String contextPath = "/quiz";
	private String imagePath;
	private String recordingPath;
	
	private int templateImageWidth;
	private int templateImageHeight;
	private int backgroundImageWidth;
	private int backgroundImageHeight;
	private int frontImageWidth;
	private int frontImageHeight;
	private int smallMetaImageWidth;
	private int smallMetaImageHeight;
	private int bigMetaImageWidth;
	private int bigMetaImageHeight;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getRecordingPath() {
		return recordingPath;
	}

	public void setRecordingPath(String recordingPath) {
		this.recordingPath = recordingPath;
	}

	public int getTemplateImageWidth() {
		return templateImageWidth;
	}

	public void setTemplateImageWidth(int templateImageWidth) {
		this.templateImageWidth = templateImageWidth;
	}

	public int getTemplateImageHeight() {
		return templateImageHeight;
	}

	public void setTemplateImageHeight(int templateImageHeight) {
		this.templateImageHeight = templateImageHeight;
	}

	public int getBackgroundImageWidth() {
		return backgroundImageWidth;
	}

	public void setBackgroundImageWidth(int backgroundImageWidth) {
		this.backgroundImageWidth = backgroundImageWidth;
	}

	public int getBackgroundImageHeight() {
		return backgroundImageHeight;
	}

	public void setBackgroundImageHeight(int backgroundImageHeight) {
		this.backgroundImageHeight = backgroundImageHeight;
	}

	public int getFrontImageWidth() {
		return frontImageWidth;
	}

	public void setFrontImageWidth(int frontImageWidth) {
		this.frontImageWidth = frontImageWidth;
	}

	public int getFrontImageHeight() {
		return frontImageHeight;
	}

	public void setFrontImageHeight(int frontImageHeight) {
		this.frontImageHeight = frontImageHeight;
	}

	public int getSmallMetaImageWidth() {
		return smallMetaImageWidth;
	}

	public void setSmallMetaImageWidth(int smallMetaImageWidth) {
		this.smallMetaImageWidth = smallMetaImageWidth;
	}

	public int getSmallMetaImageHeight() {
		return smallMetaImageHeight;
	}

	public void setSmallMetaImageHeight(int smallMetaImageHeight) {
		this.smallMetaImageHeight = smallMetaImageHeight;
	}

	public int getBigMetaImageWidth() {
		return bigMetaImageWidth;
	}

	public void setBigMetaImageWidth(int bigMetaImageWidth) {
		this.bigMetaImageWidth = bigMetaImageWidth;
	}

	public int getBigMetaImageHeight() {
		return bigMetaImageHeight;
	}

	public void setBigMetaImageHeight(int bigMetaImageHeight) {
		this.bigMetaImageHeight = bigMetaImageHeight;
	}
	
	
}
