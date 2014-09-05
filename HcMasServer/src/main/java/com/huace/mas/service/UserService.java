package com.huace.mas.service;

import java.util.List;

import com.huace.mas.entity.User;

public interface UserService {

	public List<User> findUserByNameAndPassword(String userName,String password);
}
