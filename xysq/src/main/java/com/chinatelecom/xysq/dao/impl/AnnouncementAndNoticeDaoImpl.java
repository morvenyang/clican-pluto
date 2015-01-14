package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

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
		return (List<AnnouncementAndNotice>) this
				.getHibernateTemplate()
				.findByNamedParam(
						"from AnnouncementAndNotice where community.id = :communityId and innerModule = :innerModule",
						new String[] { "communityId", "innerModule" },
						new Object[] {
								community.getId(),
								announcement ? InnerModule.ANNOUNCEMENT
										: InnerModule.NOTICE });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnnouncementAndNotice> findAnnouncementAndNotice(
			final Long communityId, final boolean announcement, final int page,
			final int pageSize) {
		return (List<AnnouncementAndNotice>) this.getHibernateTemplate()
				.executeFind(new HibernateCallback() {

					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.createQuery("from AnnouncementAndNotice where community.id = :communityId and innerModule = :innerModule order by modifyTime desc");
						query.setParameter("communityId", communityId);
						query.setParameter("innerModule",
								announcement ? InnerModule.ANNOUNCEMENT
										: InnerModule.NOTICE);
						query.setMaxResults(pageSize);
						query.setFirstResult((page - 1) * pageSize);
						return query.list();
					}

				});
	}

	@Override
	public void saveAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		this.getHibernateTemplate().saveOrUpdate(announcementAndNotice);
	}

	@Override
	public void deleteAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		this.getHibernateTemplate().delete(announcementAndNotice);
	}

}
