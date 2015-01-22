package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.dao.ForumDao;
import com.chinatelecom.xysq.model.ForumTopic;

public class ForumDaoImpl extends BaseDao implements ForumDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ForumTopic> queryTopic(final Long communityId,final int page, final int pageSize) {
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

}
