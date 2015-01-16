package com.chinatelecom.xysq.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;

import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.json.LoginJson;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User findUserByUserName(String userName) {
		return userDao.findUserByUserName(userName);
	}

	@Override
	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	@Override
	public void saveUser(User user) {
		if (user.getId() == null) {
			// set the password for new user, for user upate we don't change the
			// password in db.
			user.setPassword(DigestUtils.shaHex(user.getPassword()));
		}
		this.userDao.saveUser(user);
	}

	@Override
	public void updatePassword(User user) {
		user.setPassword(DigestUtils.shaHex(user.getPassword()));
		this.userDao.saveUser(user);
	}

	@Override
	public User findUserById(Long id) {
		return userDao.findUserById(id);
	}

	@Override
	public List<User> findAreaAdmin(String keyword) {
		return userDao.findAreaAdmin(keyword);
	}

	@Override
	public String login(String userName, String password) {
		User user = this.userDao.findUserByUserName(userName);
		LoginJson result = new LoginJson();
		if (user == null) {
			result.setSuccess(false);
			result.setMessage("该用户名不存在");
		}else{
			if(!user.getPassword().equals(DigestUtils.shaHex(password))){
				result.setSuccess(false);
				result.setMessage("密码错误");
			}else{
				result.setSuccess(true);
				result.setMessage("登录成功");
			}
		}
		return JSONObject.fromObject(result).toString();
	}

	@Override
	public void register(String userName, String password, String msisdn,
			String verifyCode) {
		// TODO Auto-generated method stub

	}

}
