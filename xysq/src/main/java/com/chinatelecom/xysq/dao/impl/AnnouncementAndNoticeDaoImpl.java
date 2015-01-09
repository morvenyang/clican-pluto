package com.chinatelecom.xysq.dao.impl;

import java.util.List;

import com.chinatelecom.xysq.dao.AnnouncementAndNoticeDao;
import com.chinatelecom.xysq.enumeration.InnerModule;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.Community;

public class AnnouncementAndNoticeDaoImpl extends BaseDao implements
		AnnouncementAndNoticeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AnnouncementAndNotice> findAnnouncementAndNotice(
			Community community, boolean announcement) {
		return (List<AnnouncementAndNotice>)this.getHibernateTemplate()
				.findByNamedParam(
						"from AnnouncementAndNotice where community.id = :communityId and innerModule = :innerModule",
						new String[] { "communityId", "innerModule" },
						new Object[] {
								community.getId(),
								announcement ? InnerModule.ANNOUNCEMENT
										: InnerModule.NOTICE });
	}

	@Override
	public void saveAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		this.getHibernateTemplate().saveOrUpdate(announcementAndNotice);
	}

}
