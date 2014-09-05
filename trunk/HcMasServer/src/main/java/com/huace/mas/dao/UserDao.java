package com.huace.mas.dao;

import java.util.List;

import com.huace.mas.entity.User;

public interface UserDao {
	
	public List<User> findUserByNameAndPassword(String userName,String password);

}
