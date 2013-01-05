package com.clican.appletv.common;

import org.springframework.stereotype.Component;

public class SpringProperty {

	private String systemServerUrl;

	private String tudouSessionid;

	private String tudouRecommendApi;

	private String tudouChannelApi;

	public String getSystemServerUrl() {
		return systemServerUrl;
	}

	public void setSystemServerUrl(String systemServerUrl) {
		this.systemServerUrl = systemServerUrl;
	}

	public String getTudouSessionid() {
		return tudouSessionid;
	}

	public void setTudouSessionid(String tudouSessionid) {
		this.tudouSessionid = tudouSessionid;
	}

	public String getTudouRecommendApi() {
		return tudouRecommendApi;
	}

	public void setTudouRecommendApi(String tudouRecommendApi) {
		this.tudouRecommendApi = tudouRecommendApi;
	}

	public String getTudouChannelApi() {
		return tudouChannelApi;
	}

	public void setTudouChannelApi(String tudouChannelApi) {
		this.tudouChannelApi = tudouChannelApi;
	}

}
