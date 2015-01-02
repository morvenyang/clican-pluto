package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.BroadbandRemindDao;
import com.chinatelecom.xysq.model.BroadbandRemind;

public class BroadbandRemindDaoImpl extends BaseDao implements
		BroadbandRemindDao {

	@SuppressWarnings("unchecked")
	@Override
	public PageList<BroadbandRemind> findBroadbandRemind(final int page, final int pageSize) {
		return (PageList<BroadbandRemind>) this.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql = "from BroadbandRemind";
						Query query = session.createQuery(hsql);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<BroadbandRemind> list = query.list();

						Query queryCount = session
								.createQuery("select count(*) from BroadbandRemind");
						Long count = (Long) queryCount.uniqueResult();
						return new PageList<BroadbandRemind>(list, page, pageSize,
								count.intValue(), new BroadbandRemind());
					}
				});
	}

}
