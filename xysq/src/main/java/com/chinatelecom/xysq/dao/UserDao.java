package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.model.User;

public interface UserDao {

	public User findUserByUserName(String userName);

	public List<User> findAllUsers();

	public void saveUser(User user);

}
