package com.chinatelecom.xysq.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.model.User;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

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

	
}
