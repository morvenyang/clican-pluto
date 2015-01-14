package com.chinatelecom.xysq.service;

public interface IndexService {

	
	public String queryIndex(Long communityId);
	
	public String queryAnnouncementAndNotice(Long communityId,boolean announcement,int page,int pageSize);
}
