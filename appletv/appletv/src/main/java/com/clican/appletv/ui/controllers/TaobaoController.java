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
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.domain.TaobaokeItemDetail;
import com.taobao.api.request.TaobaokeItemsDetailGetRequest;
import com.taobao.api.request.TaobaokeItemsGetRequest;
import com.taobao.api.response.TaobaokeItemsDetailGetResponse;
import com.taobao.api.response.TaobaokeItemsGetResponse;

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

	@RequestMapping("/taobao/itemList.xml")
	public String itemListPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "cid", required = false) Long cid,
			@RequestParam(value = "page", required = false) Long page)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access item list for cid:" + cid);
		}

		if (page == null) {
			page = 0L;
		}
		TaobaokeItemsGetRequest req = new TaobaokeItemsGetRequest();
		req.setFields("num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume");
		req.setCid(cid);
		req.setPageNo(page);
		req.setPageSize(40L);
		TaobaokeItemsGetResponse resp = taobaoRestClient.execute(req);
		long totalPage = (resp.getTotalResults() - 1) / req.getPageSize();
		if (totalPage > 10) {
			totalPage = 10;
		}
		List<TaobaokeItem> itemList = resp.getTaobaokeItems();

		long begin, end = 0;
		if (page < totalPage - 10) {
			begin = page;
			end = page + 9;
		} else {
			end = totalPage;
			begin = totalPage - 9;
		}
		request.setAttribute("pagiurl", springProperty.getSystemServerUrl()
				+ "/ctl/taobao/itemList.xml?cid=" + cid);
		request.setAttribute("itemList", itemList);
		request.setAttribute("begin", begin);
		request.setAttribute("end", end);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/itemList";
	}

	@RequestMapping("/taobao/item.xml")
	public String itemPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "itemId", required = false) Long itemId)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access item :" + itemId);
		}

		TaobaokeItemsDetailGetRequest req = new TaobaokeItemsDetailGetRequest();
		req.setFields("click_url,shop_click_url,seller_credit_score,num_iid,title,nick");
		req.setNumIids(itemId.toString());
		TaobaokeItemsDetailGetResponse resp = taobaoRestClient.execute(req);
		List<TaobaokeItemDetail> list = resp.getTaobaokeItemDetails();
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		if (list.size() == 0) {
			return "taobao/noresult";
		} else {
			request.setAttribute("itemDetail", list.get(0));
			return "taobao/item";
		}
	}
}
