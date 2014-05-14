package com.peacebird.dataserver.service;

import com.peacebird.dataserver.model.User;

public interface UserService {

	public User findUserByUserName(String userName);
}
