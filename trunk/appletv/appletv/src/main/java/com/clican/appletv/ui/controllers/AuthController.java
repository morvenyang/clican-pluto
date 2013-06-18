package com.clican.appletv.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

	@RequestMapping("/auth/login.do")
	public String login(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "password", required = false) String password) {
		if (StringUtils.isNotEmpty(userName)
				&& StringUtils.isNotEmpty(password)) {
			if (userName.equals("admin")
					&& DigestUtils.md5Hex(password).equals(
							"66a789ff51072626f1b11f89d6b09c69")) {
				return "admin/index";
			}
		}
		request.setAttribute("message", "用户名密码错误");
		return "auth/login";
	}

}
