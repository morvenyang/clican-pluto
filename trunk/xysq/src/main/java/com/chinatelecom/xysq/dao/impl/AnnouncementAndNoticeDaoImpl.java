package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.dao.AnnouncementAndNoticeDao;
import com.chinatelecom.xysq.enumeration.InnerModule;
import com.chinatelecom.xysq.enumeration.NoticeCategory;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.AnnouncementAndNoticeContent;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.model.Image;

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
			final Long communityId, final boolean announcement,final NoticeCategory noticeCategory, final int page,
			final int pageSize) {
		return (List<AnnouncementAndNotice>) this.getHibernateTemplate()
				.executeFind(new HibernateCallback() {

					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql;
						if(announcement){
							hsql = "from AnnouncementAndNotice where community.id = :communityId and innerModule = :innerModule order by modifyTime desc";
						}else{
							hsql = "from AnnouncementAndNotice where community.id = :communityId and innerModule = :innerModule and noticeCategory =:noticeCategory order by modifyTime desc";
						}
						Query query = session.createQuery(hsql);
						query.setParameter("communityId", communityId);
						query.setParameter("innerModule",
								announcement ? InnerModule.ANNOUNCEMENT
										: InnerModule.NOTICE);
						if(!announcement){
							query.setParameter("noticeCategory",noticeCategory);
						}
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
	public void saveAnnouncementAndNoticeContent(
			AnnouncementAndNoticeContent announcementAndNoticeContent) {
		this.getHibernateTemplate().saveOrUpdate(announcementAndNoticeContent);
	}

	@Override
	public void deleteAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		this.getHibernateTemplate().delete(announcementAndNotice);
	}

	@Override
	public AnnouncementAndNotice findById(Long id) {
		return (AnnouncementAndNotice)this.getHibernateTemplate().get(AnnouncementAndNotice.class, id);
	}

	@Override
	public void deleteContent(final Long announcementAndNoticeId,
			final List<Long> excludeIds) {
		this.getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hsql;
				if(excludeIds==null||excludeIds.size()==0){
					hsql = "delete from AnnouncementAndNoticeContent where announcementAndNotice.id = :announcementAndNoticeId";
				}else{
					hsql = "delete from AnnouncementAndNoticeContent where announcementAndNotice.id = :announcementAndNoticeId and id not in (:excludeIds)";
				}
				Query query = session.createQuery(hsql);
				query.setParameter("announcementAndNoticeId", announcementAndNoticeId);
				if(excludeIds!=null&&excludeIds.size()>0){
					query.setParameterList("excludeIds", excludeIds);
				}
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void saveImage(Image image) {
		this.getHibernateTemplate().saveOrUpdate(image);
	}
	
	
}
