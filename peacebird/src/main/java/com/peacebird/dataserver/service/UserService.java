package com.peacebird.dataserver.service;

import java.util.List;

import com.peacebird.dataserver.model.User;

public interface UserService {

	public User findUserByUserName(String userName);
	
	public List<User> findAllUsers();

	public void saveUser(User user);
	
	public void updatePassword(User user);
	
	public void deleteUser(User user);
	
	public List<User> findUsers(String keyword);
}
