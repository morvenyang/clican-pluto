package com.ikidstv.quiz.dao;

import com.ikidstv.quiz.model.User;

public interface UserDao {

	public User findUserByAccount(String account);
}
