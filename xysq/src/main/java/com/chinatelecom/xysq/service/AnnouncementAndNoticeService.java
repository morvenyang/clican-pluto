package com.chinatelecom.xysq.service;

import java.util.List;
import java.util.Set;

import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.Community;

public interface AnnouncementAndNoticeService {

	
	public List<AnnouncementAndNotice> findAnnouncementAndNotice(Community community,boolean announcement);
	
	public void publishAnnouncementAndNotice(AnnouncementAndNotice announcementAndNotice,Set<Long> communityIds);
	
	public void saveAnnouncementAndNotice(AnnouncementAndNotice announcementAndNotice);
	
	public void deleteAnnouncementAndNotice(AnnouncementAndNotice announcementAndNotice);
	
	public AnnouncementAndNotice findById(Long id);
	
}
