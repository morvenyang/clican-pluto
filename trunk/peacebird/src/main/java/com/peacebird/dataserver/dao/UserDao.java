package com.peacebird.dataserver.dao;

import com.peacebird.dataserver.model.User;

public interface UserDao {

	public User findUserByUserName(String userName);
}
