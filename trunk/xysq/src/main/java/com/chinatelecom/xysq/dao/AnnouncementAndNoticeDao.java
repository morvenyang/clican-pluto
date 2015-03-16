package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.enumeration.NoticeCategory;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.AnnouncementAndNoticeContent;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.model.Image;

public interface AnnouncementAndNoticeDao {

	public List<AnnouncementAndNotice> findAnnouncementAndNotice(
			Community community, boolean announcement);
	
	public List<AnnouncementAndNotice> findAnnouncementAndNotice(
			Long communityId, boolean announcement,NoticeCategory noticeCategory,int page,int pageSize);

	public void saveAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice);
	
	public void saveAnnouncementAndNoticeContent(
			AnnouncementAndNoticeContent announcementAndNoticeContent);
	
	public void deleteAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice);
	
	public AnnouncementAndNotice findById(Long id);
	
	public void deleteContent(Long announcementAndNoticeId,List<Long> excludeIds);
	
	public void saveImage(Image image);
	
}
