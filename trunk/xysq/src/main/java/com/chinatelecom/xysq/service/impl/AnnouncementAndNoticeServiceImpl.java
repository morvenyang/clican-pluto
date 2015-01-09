package com.chinatelecom.xysq.service.impl;

import java.util.List;

import com.chinatelecom.xysq.dao.AnnouncementAndNoticeDao;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.service.AnnouncementAndNoticeService;

public class AnnouncementAndNoticeServiceImpl implements
		AnnouncementAndNoticeService {
	
	private AnnouncementAndNoticeDao announcementAndNoticeDao;

	public void setAnnouncementAndNoticeDao(
			AnnouncementAndNoticeDao announcementAndNoticeDao) {
		this.announcementAndNoticeDao = announcementAndNoticeDao;
	}

	@Override
	public List<AnnouncementAndNotice> findAnnouncementAndNotice(
			Community community, boolean announcement) {
		return announcementAndNoticeDao.findAnnouncementAndNotice(community, announcement);
	}
	
	

}
