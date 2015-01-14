package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.Community;

public interface AnnouncementAndNoticeDao {

	public List<AnnouncementAndNotice> findAnnouncementAndNotice(
			Community community, boolean announcement);
	
	public List<AnnouncementAndNotice> findAnnouncementAndNotice(
			Long communityId, boolean announcement,int page,int pageSize);

	public void saveAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice);
	
	public void deleteAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice);
}
