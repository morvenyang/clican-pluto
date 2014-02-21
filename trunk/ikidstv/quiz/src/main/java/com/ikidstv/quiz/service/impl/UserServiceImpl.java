package com.ikidstv.quiz.service.impl;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.ikidstv.quiz.dao.UserDao;
import com.ikidstv.quiz.model.User;
import com.ikidstv.quiz.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User login(String account, String password) {
		String hashPassword = DigestUtils.shaHex(password);
		User user = userDao.findUserByAccount(account);
		if (user != null && user.getPassword().equals(hashPassword)) {
			return user;
		} else {
			return null;
		}
	}

	public boolean checkAccountExisted(String account) {
		User user = userDao.findUserByAccount(account);
		if (user != null) {
			return true;
		} else {
			return false;
		}
	}

	public void saveUser(User user) {
		if (user.getId() == null) {
			// set the password for new user, for user upate we don't change the
			// password in db.
			user.setPassword(DigestUtils.shaHex(user.getPassword()));
		}
		this.userDao.saveUser(user);
	}

	public void updatePassword(User user) {
		user.setPassword(DigestUtils.shaHex(user.getPassword()));
		this.userDao.saveUser(user);
	}

	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

}
