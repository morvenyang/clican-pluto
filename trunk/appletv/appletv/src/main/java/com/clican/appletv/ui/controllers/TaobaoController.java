package com.clican.appletv.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlipaySystemOauthTokenRequest;
import com.taobao.api.response.AlipaySystemOauthTokenResponse;

@Controller
public class TaobaoController {

	public final static String TAOBAO_TOKEN_NAME = "taobaoAccessToken";
	public final static String TAOBAO_USER_ID_NAME = "taobaoUserId";

	@Autowired
	@Qualifier("taobaoAuthClient")
	private TaobaoClient taobaoAuthClient;

	@Autowired
	@Qualifier("taobaoRestClient")
	private TaobaoClient taobaoRestClient;

	@Autowired
	private SpringProperty springProperty;

	private final static Log log = LogFactory.getLog(TaobaoController.class);

	@RequestMapping("/taobao/oaAuthCallback.do")
	public String callback(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "code", required = false) String code)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("code=" + code);
		}
		AlipaySystemOauthTokenRequest req = new AlipaySystemOauthTokenRequest();
		req.setCode(code);
		req.setGrantType("authorization_code");
		AlipaySystemOauthTokenResponse resp = taobaoAuthClient.execute(req);
		String accessToken = resp.getAccessToken();
		String userId = resp.getAlipayUserId();
		if (log.isDebugEnabled()) {
			log.debug("set access token[" + accessToken + "]");
		}
		request.getSession().setAttribute(TAOBAO_TOKEN_NAME, accessToken);
		request.getSession().setAttribute(TAOBAO_USER_ID_NAME, userId);
		return "taobao/oaAuthCallback";
	}

	@RequestMapping("/taobao/login.do")
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.sendRedirect(springProperty.getTaobaoLoginUrl());
	}
}
