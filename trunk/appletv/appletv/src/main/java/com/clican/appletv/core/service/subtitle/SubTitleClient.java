package com.clican.appletv.core.service.subtitle;

public interface SubTitleClient {

	public String generateHash(String fileUrl);
	
	public String downloadSubTitle(String fileUrl);
}
