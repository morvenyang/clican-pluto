package com.ikidstv.quiz.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ikidstv.quiz.dao.UserDao;
import com.ikidstv.quiz.model.User;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	@SuppressWarnings("unchecked")
	public User findUserByAccount(String account) {
		List<User> users = this.getHibernateTemplate().findByNamedParam("from User where account = :account", "account", account);
		if(users.size()!=0){
			return users.get(0);
		}else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() {
		List<User> users = this.getHibernateTemplate().find("from User");
		return users;
	}

	public void saveUser(User user) {
		this.getHibernateTemplate().saveOrUpdate(user);
	}

}
