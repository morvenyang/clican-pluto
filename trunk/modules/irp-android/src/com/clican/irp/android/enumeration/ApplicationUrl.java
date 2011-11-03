package com.clican.irp.android.enumeration;

public enum ApplicationUrl {

	LOGIN_SERVER_LIST("http://irpcenter.gildata.com/investerminal/apple/customer.json"),
	
	QUERY_REPORT("apple/report/query.do"),
	
	READ_REPORT("apple/report/read.do"),
	
	DOWNLOAD_REPORT("apple/report/download.do");

	private String url;

	private ApplicationUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
