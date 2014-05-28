package com.peacebird.dataserver.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
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
			@RequestParam(value = "token", required = false) String token,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("userName=" + userName + " login with token="+token);
		}
		LoginResult lr = null;
		User user = userService.findUserByUserName(userName);
		if (user == null) {
			lr = new LoginResult(1000, "用户名不存在", -1);
		} else {
			String hashPassword = DigestUtils.shaHex(password);
			if (user.getPassword().equals(hashPassword)) {
				req.getSession().setAttribute("user", user);
				if (StringUtils.isNotEmpty(token)) {
					user.setToken(token);
					userService.saveUser(user);
				}
				lr = new LoginResult(0, "登录成功", user.getExpiredDays());
			} else {
				lr = new LoginResult(1001, "密码错误", -1);
			}
		}
		lr.setJsessionid(req.getSession().getId());
		String result = JSONObject.fromObject(lr).toString();
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private String getNotLoginResult() {
		String notLogin = "{\"result\":1002,\"message\":\"未登录\"}";
		return notLogin;
	}

	private String getErrorResult(int code, String message) {
		return "{\"result\":" + code + ",\"message\":\"" + message + "\"}";
	}

	@RequestMapping("/index")
	public void index(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		String result;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			String brands = user.getBrands();
			if (StringUtils.isEmpty(brands)) {
				result = getErrorResult(3001, "当前用户没有可查阅的品牌,请让管理员设置品牌查阅权限");
			} else {
				result = this.dataService.getIndexResult(brands.split(","));
			}
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/brand")
	public void brand(@RequestParam(value = "brand") String brand,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			result = this.dataService.getBrandResult(brand);
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/retail")
	public void retail(@RequestParam(value = "brand") String brand,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			result = this.dataService.getRetailResult(brand);
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * kpiView
	 * @param brand
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/channel")
	public void channel(@RequestParam(value = "brand") String brand,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			result = this.dataService.getChannelResult(brand);
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/storeRank")
	public void storeRank(@RequestParam(value = "brand") String brand,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			result = this.dataService.getStoreRankResult(brand);
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
