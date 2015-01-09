package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.PosterDao;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.User;

public class PosterDaoImpl extends BaseDao implements PosterDao {

	@Override
	public Poster findPosterById(Long id) {
		return (Poster) this.getHibernateTemplate().get(Poster.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageList<Poster> findPoster(final int page, final int pageSize) {
		return (PageList<Poster>) this.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql = "from Poster order by createTime desc";
						Query query = session.createQuery(hsql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Poster> list = query.list();

						Query queryCount = session
								.createQuery("select count(*) from Poster");
						Long count = (Long) queryCount.uniqueResult();
						return new PageList<Poster>(list, page, pageSize, count
								.intValue(), new Poster());
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageList<Poster> findPosterByOwner(final User owner, final int page,
			final int pageSize) {
		return (PageList<Poster>) this.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql = "from Poster where owner.id = :ownerId order by createTime desc";
						Query query = session.createQuery(hsql);
						query.setParameter("ownerId", owner.getId());
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Poster> list = query.list();

						Query queryCount = session
								.createQuery("select count(*) from Poster where owner.id= :ownerId");
						queryCount.setParameter("ownerId", owner.getId());
						Long count = (Long) queryCount.uniqueResult();
						return new PageList<Poster>(list, page, pageSize, count
								.intValue(), new Poster());
					}
				});
	}

	@Override
	public void savePoster(Poster poster) {
		this.getHibernateTemplate().saveOrUpdate(poster);
	}

	@Override
	public void deletePoster(Poster poster) {
		this.getHibernateTemplate().delete(poster);
	}

}
