package com.chinatelecom.xysq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinatelecom.xysq.dao.AnnouncementAndNoticeDao;
import com.chinatelecom.xysq.dao.AreaDao;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
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

	@Override
	public void publishAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice, Set<Long> communityIds) {
		List<Community> communityList = areaDao
				.findCommunityByIds(communityIds);
		for (Community community : communityList) {
			try {
				AnnouncementAndNotice oneForCommunity = (AnnouncementAndNotice)BeanUtils
						.cloneBean(announcementAndNotice);
				oneForCommunity.setCommunity(community);
				oneForCommunity.setCreateTime(new Date());
				oneForCommunity.setModifyTime(new Date());
				this.announcementAndNoticeDao
						.saveAnnouncementAndNotice(oneForCommunity);
			} catch (Exception e) {
				log.error("", e);
			}

		}
	}

}
