package com.huace.mas.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.huace.mas.dao.UserDao;
import com.huace.mas.entity.User;

public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserByNameAndPassword(String userName, String password) {
		List<User> users = this.getJdbcTemplate().query(
				"SELECT * FROM BS_User where UserName = ? and Pwd=?",
				new String[] { userName, password }, new BeanPropertyRowMapper(User.class));
		return users;
	}

}
