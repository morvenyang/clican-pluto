package com.peacebird.dataserver.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.peacebird.dataserver.dao.UserDao;
import com.peacebird.dataserver.model.User;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUsers(String keyword) {
		return this.getHibernateTemplate().findByNamedParam("from User where userName like :keyword or tel like :keyword","keyword","%"+keyword+"%");
	}

	public void saveUser(User user) {
		this.getHibernateTemplate().saveOrUpdate(user);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findAllActiveToken() {
		return this.getHibernateTemplate().find("select token from User where active = true and role=1 and token is not null");
	}

	@Override
	public void deleteUser(User user) {
		this.getHibernateTemplate().delete(user);
	}
	
	
}
