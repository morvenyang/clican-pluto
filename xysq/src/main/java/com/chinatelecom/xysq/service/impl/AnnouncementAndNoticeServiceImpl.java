package com.chinatelecom.xysq.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinatelecom.xysq.dao.AnnouncementAndNoticeDao;
import com.chinatelecom.xysq.dao.AreaDao;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.AnnouncementAndNoticeContent;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.service.AnnouncementAndNoticeService;

public class AnnouncementAndNoticeServiceImpl implements
		AnnouncementAndNoticeService {

	private final static Log log = LogFactory
			.getLog(AnnouncementAndNoticeServiceImpl.class);

	private AnnouncementAndNoticeDao announcementAndNoticeDao;

	private AreaDao areaDao;

	public void setAnnouncementAndNoticeDao(
			AnnouncementAndNoticeDao announcementAndNoticeDao) {
		this.announcementAndNoticeDao = announcementAndNoticeDao;
	}

	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}

	@Override
	public List<AnnouncementAndNotice> findAnnouncementAndNotice(
			Community community, boolean announcement) {
		return announcementAndNoticeDao.findAnnouncementAndNotice(community,
				announcement);
	}

	public void saveAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		boolean deleteContent = announcementAndNotice.getId() != null;
		announcementAndNoticeDao
				.saveAnnouncementAndNotice(announcementAndNotice);
		List<AnnouncementAndNoticeContent> contents = announcementAndNotice
				.getContents();
		List<Long> excludeIds = new ArrayList<Long>();
		if (contents != null) {
			for (AnnouncementAndNoticeContent content : contents) {
				if (content.getId() == null) {
					if (!content.isText()) {
						announcementAndNoticeDao.saveImage(content.getImage());
					}
				} 
				content.setAnnouncementAndNotice(announcementAndNotice);
				announcementAndNoticeDao
						.saveAnnouncementAndNoticeContent(content);
				excludeIds.add(content.getId());
			}
		}
		if (deleteContent) {
			announcementAndNoticeDao.deleteContent(
					announcementAndNotice.getId(), excludeIds);
		}

	}

	@Override
	public void deleteAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		announcementAndNoticeDao
				.deleteAnnouncementAndNotice(announcementAndNotice);
	}

	@Override
	public AnnouncementAndNotice findById(Long id) {
		return announcementAndNoticeDao.findById(id);
	}

	@Override
	public void publishAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice, Set<Long> communityIds) {
		List<Community> communityList = areaDao
				.findCommunityByIds(communityIds);
		for (Community community : communityList) {
			try {
				AnnouncementAndNotice oneForCommunity = (AnnouncementAndNotice) BeanUtils
						.cloneBean(announcementAndNotice);
				oneForCommunity.setContents(this.cloneList(announcementAndNotice.getContents()));
				oneForCommunity.setCommunity(community);
				oneForCommunity.setCreateTime(new Date());
				oneForCommunity.setModifyTime(new Date());
				this.saveAnnouncementAndNotice(oneForCommunity);
			} catch (Exception e) {
				log.error("", e);
			}

		}
	}

	private List<AnnouncementAndNoticeContent> cloneList(
			List<AnnouncementAndNoticeContent> contents) throws Exception {
		List<AnnouncementAndNoticeContent> r = new ArrayList<AnnouncementAndNoticeContent>();
		for (AnnouncementAndNoticeContent c : contents) {
			r.add((AnnouncementAndNoticeContent) BeanUtils.cloneBean(c));
		}
		return r;
	}
}
