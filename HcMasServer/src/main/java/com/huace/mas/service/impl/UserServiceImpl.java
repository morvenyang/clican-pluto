package com.huace.mas.service.impl;

import java.util.List;

import com.huace.mas.dao.UserDao;
import com.huace.mas.entity.User;
import com.huace.mas.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<User> findUserByNameAndPassword(String userName, String password) {
		return userDao.findUserByNameAndPassword(userName, password);
	}

	
	
}
