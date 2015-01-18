package com.chinatelecom.xysq.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.enumeration.Role;
import com.chinatelecom.xysq.json.LoginJson;
import com.chinatelecom.xysq.json.RegisterJson;
import com.chinatelecom.xysq.json.UserJson;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.UserService;

public class UserServiceImpl implements UserService {

	private final static Log log = LogFactory.getLog(UserServiceImpl.class);

	private UserDao userDao;

	private SpringProperty springProperty;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
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
	public LoginJson login(String userName, String password) {
		User user = this.userDao.findUserByUserName(userName);
		LoginJson result = new LoginJson();
		if (user == null||user.getRole()!=Role.USER) {
			result.setSuccess(false);
			result.setMessage("该用户名不存在");
		} else {
			if (!user.getPassword().equals(DigestUtils.shaHex(password))) {
				result.setSuccess(false);
				result.setMessage("密码错误");
			} else {
				UserJson userJson = new UserJson();
				userJson.setMsisdn(user.getMsisdn());
				userJson.setUserName(user.getUserName());
				userJson.setId(user.getId());
				result.setUser(userJson);
				result.setSuccess(true);
				result.setMessage("登录成功");
			}
		}
		return result;
	}

	@Override
	public RegisterJson register(String userName, String password, String msisdn,
			String verifyCode) {
		HttpClient httpclient = new HttpClient();
		RegisterJson result = new RegisterJson();
		try {
			if (StringUtils.isNotEmpty(springProperty.getProxyHost())) {
				httpclient.getHostConfiguration().setProxy(
						springProperty.getProxyHost(),
						springProperty.getProxyPort());
			}

			PostMethod post = new PostMethod(
					"https://leancloud.cn/1.1/verifySmsCode/" + verifyCode
							+ "?mobilePhoneNumber=" + msisdn);
			post.addRequestHeader("X-AVOSCloud-Application-Id",
					"zgdiillmtdo07gx2zwu5xlhubqu0ob6jf4pmd6d80o4r63jr");
			post.addRequestHeader("X-AVOSCloud-Application-Key",
					"8nc8zg36bmlqp00auc8usbz5k641vsym4k5sanlrcclgikzr");
			post.addRequestHeader("Content-Type", "application/json");
			int code = httpclient.executeMethod(post);
			String resp = new String(post.getResponseBody(), "utf-8");
			if (log.isDebugEnabled()) {
				log.debug("verifySmsCode Resp[" + resp + "]");
			}
			if (code == HttpStatus.SC_OK) {
				User user = new User();
				user.setUserName(userName);
				user.setPassword(DigestUtils.shaHex(user.getPassword()));
				user.setActive(true);
				user.setMsisdn(msisdn);
				user.setRole(Role.USER);
				this.userDao.saveUser(user);
				UserJson userJson = new UserJson();
				userJson.setMsisdn(user.getMsisdn());
				userJson.setUserName(user.getUserName());
				result.setUser(userJson);
				result.setSuccess(true);
				result.setMessage("注册成功");
			} else {
				result.setSuccess(false);
				result.setMessage("短信验证码错误");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("系统错误,注册失败");
			log.error("", e);
		}
		return result;
	}
}
