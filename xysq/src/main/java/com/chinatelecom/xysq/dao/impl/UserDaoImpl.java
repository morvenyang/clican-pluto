package com.chinatelecom.xysq.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.enumeration.Role;
import com.chinatelecom.xysq.model.User;

public class UserDaoImpl extends BaseDao implements UserDao {

	@SuppressWarnings("unchecked")
	public User findUserByUserName(String userName) {
		List<User> result = this.getHibernateTemplate().findByNamedParam(
				"from User where userName = :userName", "userName", userName);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() {
		return this.getHibernateTemplate().find("from User");
	}

	public void saveUser(User user) {
		this.getHibernateTemplate().saveOrUpdate(user);
	}

	@Override
	public User findUserById(Long id) {
		return (User) this.getHibernateTemplate().get(User.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAreaAdmin(final String keyword) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery("from User where active=true and (role = :adminRole or role = :areaAdminRole) and userName like :keyword");
				query.setParameter("adminRole", Role.ADMIN);
				query.setParameter("areaAdminRole", Role.AREA_ADMIN);
				query.setParameter("keyword", "%"+keyword+"%");
				query.setMaxResults(20);
				return query.list();
			}
		});
	}

}
