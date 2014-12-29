package com.chinatelecom.xysq.service.impl;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User findUserByUserName(String userName) {
		return userDao.findUserByUserName(userName);
	}

	@Override
	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	@Override
	public void saveUser(User user) {
		if (user.getId() == null) {
			// set the password for new user, for user upate we don't change the
			// password in db.
			user.setPassword(DigestUtils.shaHex(user.getPassword()));
		}
		this.userDao.saveUser(user);
	}

	@Override
	public void updatePassword(User user) {
		user.setPassword(DigestUtils.shaHex(user.getPassword()));
		this.userDao.saveUser(user);
	}

}
