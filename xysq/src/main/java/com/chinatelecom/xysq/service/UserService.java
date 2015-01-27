package com.chinatelecom.xysq.service;

import java.util.List;

import com.chinatelecom.xysq.json.ProfileJson;
import com.chinatelecom.xysq.json.RegisterJson;
import com.chinatelecom.xysq.model.User;

public interface UserService {

	public User findUserByUserName(String userName);
	
	public List<User> findAllUsers();

	public void saveUser(User user);
	
	public void updatePassword(User user);
	
	public User findUserById(Long id);
	
	public List<User> findAreaAdmin(final String keyword);
	
	public ProfileJson login(String userName,String password);
	
	public ProfileJson register(String nickName,String password,String msisdn,String verifyCode);
	
	public ProfileJson forgetPassword(String password,String msisdn,String verifyCode);
	
	public ProfileJson updateProfile(Long userId,String nickName,String address,String carNumber);
}
