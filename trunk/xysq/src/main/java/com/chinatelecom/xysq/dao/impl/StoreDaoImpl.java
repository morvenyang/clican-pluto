package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.StoreDao;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.User;

public class StoreDaoImpl extends BaseDao implements StoreDao {

	@SuppressWarnings("unchecked")
	@Override
	public PageList<Store> findStoreByOwner(final User owner, final int page,
			final int pageSize) {
		return (PageList<Store>) this.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql = "from Store where owner.id= :ownerId";
						Query query = session.createQuery(hsql);
						query.setParameter("ownerId", owner.getId());
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						List<Store> list = query.list();

						Query queryCount = session
								.createQuery("select count(*) from Store");
						Long count = (Long) queryCount.uniqueResult();
						return new PageList<Store>(list, page, pageSize, count
								.intValue(), new Store());
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> findStores(final Long ownerId, final String keyword) {
		return (List<Store>) this.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hsql = null;
						if (ownerId == null) {
							hsql = "from Store where name like :keyword or pinyin like :keyword or shortPinyin like :keyword";
						} else {
							hsql = "from Store where owner.id= :ownerId and name like :keyword or pinyin like :keyword or shortPinyin like :keyword";
						}

						Query query = session.createQuery(hsql);
						query.setParameter("keyword", "%" + keyword + "%");
						if (ownerId != null) {
							query.setParameter("ownerId", ownerId);
						}
						query.setMaxResults(20);
						List<Store> list = query.list();
						return list;
					}
				});
	}

	@Override
	public Store findStoreById(Long id) {
		return (Store) this.getHibernateTemplate().get(Store.class, id);
	}

	@Override
	public void saveStore(Store store) {
		this.getHibernateTemplate().saveOrUpdate(store);
	}

	@Override
	public void saveImage(Image image) {
		this.getHibernateTemplate().saveOrUpdate(image);
	}

	@Override
	public void deleteStoreImage(final Store store,
			final Set<Long> notInImageIds) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hsql;
				if (notInImageIds != null && notInImageIds.size() > 0) {
					hsql = "delete from Image where store.id = :storeId and id not in (:ids)";
				} else {
					hsql = "delete from Image where store.id = :storeId";
				}
				Query query = session.createQuery(hsql);
				query.setParameter("storeId", store.getId());
				if (notInImageIds != null && notInImageIds.size() > 0) {
					query.setParameterList("ids", notInImageIds);
				}
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void deleteStore(Store store) {
		this.getHibernateTemplate().delete(store);
	}

}
