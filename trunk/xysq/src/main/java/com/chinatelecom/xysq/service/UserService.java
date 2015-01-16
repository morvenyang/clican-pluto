package com.chinatelecom.xysq.service;

import java.util.List;

import com.chinatelecom.xysq.model.User;

public interface UserService {

	public User findUserByUserName(String userName);
	
	public List<User> findAllUsers();

	public void saveUser(User user);
	
	public void updatePassword(User user);
	
	public User findUserById(Long id);
	
	public List<User> findAreaAdmin(final String keyword);
	
	public String login(String userName,String password);
	
	public void register(String userName,String password,String msisdn,String verifyCode);
}
