package com.peacebird.dataserver.service.impl;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.peacebird.dataserver.dao.UserDao;
import com.peacebird.dataserver.model.User;
import com.peacebird.dataserver.service.UserService;

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
		this.userDao.saveUser(user);
	}

	@Override
	public void updatePassword(User user) {
		user.setPassword(DigestUtils.shaHex(user.getPassword()));
		this.userDao.saveUser(user);
	}

}
