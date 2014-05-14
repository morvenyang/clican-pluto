package com.peacebird.dataserver.service.impl;

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

}
