package com.ikidstv.quiz.service.impl;

import org.apache.commons.codec.digest.DigestUtils;

import com.ikidstv.quiz.dao.UserDao;
import com.ikidstv.quiz.model.User;
import com.ikidstv.quiz.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User login(String account, String password) {
		String hashPassword = DigestUtils.shaHex(password);
		User user = userDao.findUserByAccount(account);
		if(user!=null&&user.getPassword().equals(hashPassword)){
			return user;
		}else{
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(DigestUtils.shaHex("123456"));
	}

}
