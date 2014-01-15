package com.ikidstv.quiz.bean;

public class SpringProperty {

	private String version;
	private String contextPath = "/quiz";
	private String imagePath;
	private String recordingPath;

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
	
	
}
