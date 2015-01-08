package com.peacebird.dataserver.dao;

import java.util.List;

import com.peacebird.dataserver.model.User;

public interface UserDao {

	public User findUserByUserName(String userName);

	public List<User> findAllUsers();
	
	public List<User> findUsers(String keyword);

	public void saveUser(User user);

	public List<String> findAllActiveToken();
	
	public void deleteUser(User user);
}
