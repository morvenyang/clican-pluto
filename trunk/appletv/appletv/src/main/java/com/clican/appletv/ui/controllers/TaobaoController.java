package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.PromotionInItem;
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.TaobaokeItemsGetRequest;
import com.taobao.api.request.UmpPromotionGetRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.TaobaokeItemsGetResponse;
import com.taobao.api.response.UmpPromotionGetResponse;

@Controller
public class TaobaoController {

	private final static Log log = LogFactory.getLog(TaobaoController.class);
	public final static String TAOBAO_TOKEN_NAME = "taobaoAccessToken";
	public final static String TAOBAO_HTML_TOKEN_NAME = "taobaoHtmlToken";
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

	@RequestMapping("/taobao/loginWithToken.do")
	public void loginWithToken(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			InputStream is = request.getInputStream();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];

			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			String content = new String(os.toByteArray(), "UTF-8");
			is.close();
			os.close();

			Parser parser = Parser.createParser(content, "utf-8");
			AndFilter tokenFilter = new AndFilter(new TagNameFilter("input"),
					new HasAttributeFilter("name", "_tb_token_"));
			NodeList list = parser.parse(tokenFilter);
			String token = null;

			if (list.size() > 0) {
				TagNode node = (TagNode) list.elementAt(0);
				token = node.getAttribute("value");
			}

			if (StringUtils.isNotEmpty(token)) {
				if (log.isDebugEnabled()) {
					log.debug("Taobao user login successfully, with token:"
							+ token);
				}
				request.getSession()
						.setAttribute(TAOBAO_HTML_TOKEN_NAME, token);
				response.getOutputStream().write("success".getBytes());
			} else {
				response.getOutputStream().write("failure".getBytes());
				if (log.isDebugEnabled()) {
					log.debug("Taobao user login failure");
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/taobao/getToken.do")
	public void getToken(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String token = (String) request.getSession().getAttribute(
				TAOBAO_HTML_TOKEN_NAME);
		if (log.isDebugEnabled()) {
			log.debug("get token from session");
		}
		if (StringUtils.isNotEmpty(token)) {
			response.getOutputStream().write(token.getBytes());
		} else {
			response.getOutputStream().write("".getBytes());
		}
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
			if (begin < 0) {
				begin = 0;
			}
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
			@RequestParam(value = "itemId", required = false) Long itemId,
			@RequestParam(value = "volume", required = false) Long volume)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access item :" + itemId);
		}

		ItemGetRequest req = new ItemGetRequest();
		req.setFields("detail_url,num_iid,title,nick,desc,location,price,post_fee,express_fee,ems_fee,item_imgs,videos,pic_url,stuff_status");
		req.setNumIid(itemId);
		ItemGetResponse resp = taobaoRestClient.execute(req);
		Item item = resp.getItem();

		UmpPromotionGetRequest req2 = new UmpPromotionGetRequest();
		req2.setItemId(itemId);
		UmpPromotionGetResponse resp2 = taobaoRestClient.execute(req2);
		List<PromotionInItem> piiList = resp2.getPromotions()
				.getPromotionInItem();
		String promotion = null;
		if (piiList != null && piiList.size() > 0) {
			promotion = piiList.get(0).getName() + ":"
					+ piiList.get(0).getItemPromoPrice() + "元";
		}

		if (log.isDebugEnabled()) {
			log.debug("detail url:" + resp.getItem().getDetailUrl());
		}

		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("promotion", promotion);
		if (item == null) {
			return "taobao/noresult";
		} else {
			item.setVolume(volume);
			request.setAttribute("item", item);
			return "taobao/item";
		}
	}

}
