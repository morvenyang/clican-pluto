package com.peacebird.dataserver.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peacebird.dataserver.bean.LoginResult;
import com.peacebird.dataserver.model.User;
import com.peacebird.dataserver.service.DataService;
import com.peacebird.dataserver.service.UserService;

@Controller
public class ClientController {

	private final static Log log = LogFactory.getLog(ClientController.class);

	private UserService userService;
	private DataService dataService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}
	
	@RequestMapping("/login")
	public void login(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("userName=" + userName + " login");
		}
		LoginResult lr = null;
		User user = userService.findUserByUserName(userName);
		if (user == null) {
			lr = new LoginResult(1000, "用户名不存在", -1);
		} else {
			String hashPassword = DigestUtils.shaHex(password);
			if (user.getPassword().equals(hashPassword)) {
				lr = new LoginResult(0, "登录成功", user.getExpiredDays());
			} else {
				lr = new LoginResult(1001, "密码错误", -1);
			}
		}
		String result = JSONObject.fromObject(lr).toString();
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	@RequestMapping("/index")
	public void index(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}
}
