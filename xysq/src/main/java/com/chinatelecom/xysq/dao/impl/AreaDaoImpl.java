package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.AdminCommunityRel;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.model.PosterCommunityRel;
import com.chinatelecom.xysq.model.StoreCommunityRel;

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
						String hsql = "from Community where city.fullName like :areaFullNameLike or city.fullName = :areaFullName order by pinyin";
						Query query = session.createQuery(hsql);
						query.setParameter("areaFullNameLike",
								area.getFullName() + "/%");
						query.setParameter("areaFullName", area.getFullName());
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Community> list = query.list();

						Query queryCount = session
								.createQuery("select count(*) from Community where city.fullName like :areaFullNameLike or city.fullName = :areaFullName");
						queryCount.setParameter("areaFullNameLike",
								area.getFullName() + "/%");
						queryCount.setParameter("areaFullName",
								area.getFullName());
						Long count = (Long) queryCount.uniqueResult();
						return new PageList<Community>(list, page, pageSize,
								count.intValue(), new Community());
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
		this.getHibernateTemplate().saveOrUpdate(area);
	}

	@Override
	public void saveCommunity(Community community) {
		this.getHibernateTemplate().saveOrUpdate(community);
	}

	@Override
	public Community findCommunityById(Long id) {
		return (Community) this.getHibernateTemplate().get(Community.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Community> findCommunityByIds(Set<Long> ids) {
		String hsql = "from Community where id in (:ids)";
		List<Community> result = this.getHibernateTemplate().findByNamedParam(
				hsql, "ids", ids);
		return result;
	}

	@Override
	public void deleteCommunity(Community community) {
		this.getHibernateTemplate().delete(community);
	}

	@Override
	public void deleteAdminCommunityRel(final Community community) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session
						.createQuery("delete from AdminCommunityRel where community.id = :id");
				query.setParameter("id", community.getId());
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void deleteStoreCommunityRel(final Community community) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session
						.createQuery("delete from StoreCommunityRel where community.id = :id");
				query.setParameter("id", community.getId());
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void deletePosterCommunityRel(final Community community) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session
						.createQuery("delete from PosterCommunityRel where community.id = :id");
				query.setParameter("id", community.getId());
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void saveAdminCommunityRel(AdminCommunityRel rel) {
		this.getHibernateTemplate().saveOrUpdate(rel);
	}

	@Override
	public void saveStoreCommunityRel(StoreCommunityRel rel) {
		this.getHibernateTemplate().saveOrUpdate(rel);
	}

	@Override
	public void savePosterCommunityRel(PosterCommunityRel rel) {
		this.getHibernateTemplate().saveOrUpdate(rel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Area> findCityAreas() {
		return this.getHibernateTemplate().find("from Area a where a.level=2 order by a.shortPinyin");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Community> findCommunityByArea(Long areaId) {
		return this.getHibernateTemplate().findByNamedParam("from Community where city.id = :areaId", "areaId", areaId);
	}

}
