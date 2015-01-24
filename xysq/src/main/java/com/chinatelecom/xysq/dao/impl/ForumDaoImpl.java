package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.dao.ForumDao;
import com.chinatelecom.xysq.model.ForumPost;
import com.chinatelecom.xysq.model.ForumTopic;
import com.chinatelecom.xysq.model.Image;

public class ForumDaoImpl extends BaseDao implements ForumDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ForumTopic> queryTopic(final Long communityId, final int page,
			final int pageSize) {
		return (List<ForumTopic>) this.getHibernateTemplate().executeFind(
				new HibernateCallback() {

					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql = "from ForumTopic where communityId = :communityId order by createTime desc";
						Query query = session.createQuery(hsql);
						query.setParameter("communityId", communityId);
						query.setMaxResults(pageSize);
						query.setFirstResult((page - 1) * pageSize);
						return query.list();
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ForumPost> queryPost(final Long topicId, final int page,
			final int pageSize) {
		return (List<ForumPost>) this.getHibernateTemplate().executeFind(
				new HibernateCallback() {

					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql = "from ForumPost where forumTopic.id = :topicId order by createTime desc";
						Query query = session.createQuery(hsql);
						query.setParameter("topicId", topicId);
						query.setMaxResults(pageSize);
						query.setFirstResult((page - 1) * pageSize);
						return query.list();
					}
				});
	}

	@Override
	public ForumTopic findTopicById(Long id) {
		return (ForumTopic) this.getHibernateTemplate().get(ForumTopic.class,
				id);
	}

	@Override
	public ForumPost findPostById(Long id) {
		return (ForumPost) this.getHibernateTemplate().get(ForumPost.class,
				id);
	}

	@Override
	public void deleteImagesForTopic(final Long topicId) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session
						.createQuery("delete from Image where forumTopic.id = :topicId");
				query.setParameter("topicId", topicId);
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void deleteImagesForPost(final Long postId) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session
						.createQuery("delete from Image where forumPost.id = :postId");
				query.setParameter("postId", postId);
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void saveImage(Image image) {
		this.getHibernateTemplate().saveOrUpdate(image);
	}

	@Override
	public void saveForumTopic(ForumTopic forumTopic) {
		this.getHibernateTemplate().saveOrUpdate(forumTopic);
	}

	@Override
	public void saveForumPost(ForumPost forumPost) {
		this.getHibernateTemplate().saveOrUpdate(forumPost);
	}

}
