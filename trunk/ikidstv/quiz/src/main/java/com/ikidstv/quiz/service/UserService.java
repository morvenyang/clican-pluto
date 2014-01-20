package com.ikidstv.quiz.service;

import java.util.List;

import com.ikidstv.quiz.model.User;

public interface UserService {

	public User login(String account,String password);
	
	public List<User> findAllUsers();
	
	public void saveUser(User user);
	
}
