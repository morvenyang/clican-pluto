package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
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
	public PageList<BroadbandRemind> findBroadbandRemind(final int page,
			final int pageSize) {
		return (PageList<BroadbandRemind>) this.getHibernateTemplate()
				.executeFind(new HibernateCallback() {
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
						return new PageList<BroadbandRemind>(list, page,
								pageSize, count.intValue(),
								new BroadbandRemind());
					}
				});
	}

	@Override
	public void save(BroadbandRemind bbr) {
		this.getHibernateTemplate().saveOrUpdate(bbr);
	}

	@Override
	public void delete(BroadbandRemind bbr) {
		this.getHibernateTemplate().delete(bbr);
	}

	@Override
	public BroadbandRemind findBroadbandRemindById(Long id) {
		return (BroadbandRemind) this.getHibernateTemplate().get(
				BroadbandRemind.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BroadbandRemind> findBroadbandRemindByMsisdns(
			List<String> msisdns) {
		final List<List<String>> inIds = this.getInIds(msisdns);
		
		return this.getHibernateTemplate().executeFind(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				List<BroadbandRemind> result = new ArrayList<BroadbandRemind>();
				for(List<String> ids : inIds){
					Query query = session.createQuery("from BroadbandRemind where msisdn in (:msisdns)");
					query.setParameterList("msisdns", ids);
					List<BroadbandRemind> list = query.list();
					result.addAll(list);
				}
				return result;
			}
		});
	}

}
