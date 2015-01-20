package com.chinatelecom.xysq.service;

import com.chinatelecom.xysq.enumeration.NoticeCategory;

public interface IndexService {

	
	public String queryIndex(Long communityId);
	
	public String queryAnnouncementAndNotice(Long communityId,boolean announcement,NoticeCategory noticeCategory,int page,int pageSize);
	
	public String queryBroadbandRemind(String msisdn);
	
}
