package com.clican.appletv.ui.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;
import com.taobao.api.TaobaoClient;

@Controller
public class TaobaoController {

	private final static Log log = LogFactory.getLog(TaobaoController.class);
	public final static String TAOBAO_TOKEN_NAME = "taobaoAccessToken";
	public final static String TAOBAO_USER_ID_NAME = "taobaoUserId";

	@Autowired
	private com.clican.appletv.core.service.TaobaoClient taobaoClient;

	@Autowired
	private TaobaoClient taobaoRestClient;

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/taobao/oaAuthCallback.do")
	public String callback(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "code", required = false) String code)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("code=" + code);
		}
		TaobaoAccessToken accessToken = taobaoClient.getAccessToken(code);
		if (log.isDebugEnabled()) {
			log.debug("set access token[" + accessToken + "]");
		}
		request.getSession().setAttribute(TAOBAO_TOKEN_NAME,
				accessToken.getAccessToken());
		request.getSession().setAttribute(TAOBAO_TOKEN_NAME,
				accessToken.getUserId());
		return "taobao/oaAuthCallback";
	}

	@RequestMapping("/taobao/login.do")
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.sendRedirect(springProperty.getTaobaoLoginUrl());
	}

	@RequestMapping("/taobao/category.xml")
	public String categoryPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "parentId", required = false) Long parentId)
			throws Exception {
		if (log.isDebugEnabled()) {
			if (parentId == null) {
				log.debug("access taobao top category");
			} else {
				log.debug("access taobao sub category for parentId:" + parentId);
			}
		}
		List<TaobaoCategory> categoryList = null;
		if (parentId == null) {
			categoryList = taobaoClient.getTopCategories();
		} else {
			categoryList = taobaoClient.getCategories(parentId);
		}
		request.setAttribute("categoryList", categoryList);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/category";
	}
}
