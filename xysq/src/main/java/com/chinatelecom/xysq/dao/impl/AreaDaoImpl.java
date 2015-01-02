package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.bean.PageList;
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
	public PageList<Community> findCommunityByArea(final Area area,
			final int page, final int pageSize) {
		return (PageList<Community>) this.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql = "from Community where city.fullName like :areaName";
						Query query = session.createQuery(hsql);
						query.setParameter("areaName", area.getFullName() + "%");
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Community> list = query.list();

						Query queryCount = session
								.createQuery("select count(*) from Community where city.fullName like :areaName");
						queryCount.setParameter("areaName", area.getFullName()
								+ "%");
						Integer count = (Integer) queryCount.uniqueResult();
						return new PageList<Community>(list, page, pageSize,
								count, new Community());
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getAreasByFullNames(List<String> fullNames) {
		List<Area> areas = new ArrayList<Area>();
		List<List<String>> pagedFullNames = this.getInIds(fullNames);
		String hsql = "from Area where fullName in (:fullNames)";
		for (List<String> fns : pagedFullNames) {
			List<Area> as = this.getHibernateTemplate().findByNamedParam(hsql,
					"fullNames", fns);
			areas.addAll(as);
		}
		return areas;
	}

	@Override
	public void saveArea(Area area) {
		this.getHibernateTemplate().save(area);
	}

	@Override
	public void saveCommunity(Community community) {
		this.getHibernateTemplate().save(community);
	}

}
