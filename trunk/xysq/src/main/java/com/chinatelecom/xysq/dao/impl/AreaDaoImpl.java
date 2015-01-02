package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.bean.PagingList;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;

public class AreaDaoImpl extends BaseDao implements
		com.chinatelecom.xysq.dao.AreaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getAreaTrees() {
		return this.getHibernateTemplate().find("from Area where level=1");
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagingList<Community> findCommunityByArea(final Area area,
			final int page, final int pageSize) {
		return (PagingList<Community>)this.getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hsql = "from Community where city.fullName like :areaName";
				Query query = session.createQuery(hsql);
				query.setParameter("areaName", area.getFullName() + "%");
				query.setFirstResult((page-1) * pageSize);
				query.setMaxResults(pageSize);
				List<Community> list = query.list();
				
				Query queryCount = session.createQuery("select count(*) from Community where city.fullName like :areaName");
				queryCount.setParameter("areaName", area.getFullName() + "%");
				Integer count = (Integer)queryCount.uniqueResult();
				return new PagingList<Community>(list, page,pageSize,count,new Community());
			}
		});
	}

	

}
