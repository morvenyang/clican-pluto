package com.ikidstv.quiz.dao;

import java.util.List;

import com.ikidstv.quiz.model.User;

public interface UserDao {

	public User findUserByAccount(String account);
	
	public List<User> findAllUsers();
	
	public void saveUser(User user);
	
}
