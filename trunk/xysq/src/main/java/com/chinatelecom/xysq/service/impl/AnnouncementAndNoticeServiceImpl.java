package com.chinatelecom.xysq.service.impl;

import com.chinatelecom.xysq.dao.AnnouncementAndNoticeDao;
import com.chinatelecom.xysq.service.AnnouncementAndNoticeService;

public class AnnouncementAndNoticeServiceImpl implements
		AnnouncementAndNoticeService {
	
	private AnnouncementAndNoticeDao announcementAndNoticeDao;

	public void setAnnouncementAndNoticeDao(
			AnnouncementAndNoticeDao announcementAndNoticeDao) {
		this.announcementAndNoticeDao = announcementAndNoticeDao;
	}
	
	

}
