package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.util.NodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;
import com.clican.appletv.core.service.TaobaoClientImpl;
import com.clican.appletv.ext.htmlparser.StrongTag;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.PromotionInItem;
import com.taobao.api.domain.SellerCat;
import com.taobao.api.domain.Shop;
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.SellercatsListGetRequest;
import com.taobao.api.request.ShopGetRequest;
import com.taobao.api.request.TaobaokeItemsGetRequest;
import com.taobao.api.request.TaobaokeItemsRelateGetRequest;
import com.taobao.api.request.UmpPromotionGetRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.SellercatsListGetResponse;
import com.taobao.api.response.ShopGetResponse;
import com.taobao.api.response.TaobaokeItemsGetResponse;
import com.taobao.api.response.TaobaokeItemsRelateGetResponse;
import com.taobao.api.response.UmpPromotionGetResponse;

@Controller
public class TaobaoController {

	private final static Log log = LogFactory.getLog(TaobaoController.class);
	public final static String TAOBAO_TOKEN_NAME = "taobaoAccessToken";
	public final static String TAOBAO_HTML_TOKEN_NAME = "taobaoHtmlToken";
	public final static String TAOBAO_USER_ID_NAME = "taobaoUserId";
	public final static String TAOBAO_SELLER_CATEGORY_LIST = "taobaoSellerCategoryList";

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
			String content = this.getContent(request);

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

	@RequestMapping("/taobao/shop.xml")
	public String shopPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "nick", required = false) String nick)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access shop:" + nick);
		}

		request.getSession().removeAttribute(TAOBAO_SELLER_CATEGORY_LIST);
		ShopGetRequest shopReq = new ShopGetRequest();
		shopReq.setFields("sid,cid,title,nick,desc,bulletin,pic_path,created,modified");
		shopReq.setNick(nick);
		ShopGetResponse shopResp = taobaoRestClient.execute(shopReq);

		Shop shop = shopResp.getShop();
		Long sid = shop.getSid();
		request.setAttribute("shop", shop);
		String shopUrl = "http://shop" + sid + ".taobao.com";
		String content = ((TaobaoClientImpl) taobaoClient).httpGet(shopUrl);

		int start = content.indexOf("userid=") + "userid=".length();
		int end = content.indexOf(";", start);
		Long sellerId = Long.parseLong(content.substring(start, end).trim());

		request.setAttribute("sellerId", sellerId);
		request.setAttribute("shopId", sid);
		request.setAttribute("nick", nick);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/shop";
	}

	@RequestMapping("/taobao/shopHome.xml")
	public String shopHomePage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "sellerId", required = false) Long sellerId)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access shop home:" + sellerId);
		}

		TaobaokeItemsRelateGetRequest itemReq = new TaobaokeItemsRelateGetRequest();
		itemReq.setRelateType(4L);
		itemReq.setSellerId(sellerId);
		itemReq.setMaxCount(40L);
		itemReq.setFields("num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume");
		TaobaokeItemsRelateGetResponse itemResp = taobaoRestClient
				.execute(itemReq);

		List<TaobaokeItem> itemList = itemResp.getTaobaokeItems();

		request.setAttribute("itemList", itemList);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/shopHome";
	}

	@RequestMapping("/taobao/shopDetail.xml")
	public String shopDetailPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "nick", required = false) String nick)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access shop detail:" + nick);
		}
		ShopGetRequest shopReq = new ShopGetRequest();
		shopReq.setFields("sid,cid,title,nick,desc,bulletin,pic_path,created,modified");
		shopReq.setNick(nick);
		ShopGetResponse shopResp = taobaoRestClient.execute(shopReq);

		request.setAttribute("shop", shopResp.getShop());
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/shopDetail";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/taobao/shopCategory.xml")
	public String shopCategoryPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "nick", required = false) String nick,
			@RequestParam(value = "parentId", required = false) Long parentId)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access shop category:" + nick);
		}

		if (parentId == null) {
			parentId = 0L;
		}
		List<SellerCat> categoryList = (List<SellerCat>) request.getSession()
				.getAttribute(TAOBAO_SELLER_CATEGORY_LIST);
		if (categoryList == null) {
			SellercatsListGetRequest sellerCatReq = new SellercatsListGetRequest();
			sellerCatReq.setNick(nick);
			SellercatsListGetResponse sellerCatResp = taobaoRestClient
					.execute(sellerCatReq);
			categoryList = sellerCatResp.getSellerCats();
			request.getSession().setAttribute(TAOBAO_SELLER_CATEGORY_LIST, categoryList);
		}

		Map<Long, TaobaoCategory> categoryMap = new HashMap<Long, TaobaoCategory>();
		List<TaobaoCategory> list = new ArrayList<TaobaoCategory>();
		for (SellerCat sc : categoryList) {
			TaobaoCategory tc = new TaobaoCategory();
			tc.setId(sc.getCid());
			tc.setPicUrl(sc.getPicUrl());
			tc.setTitle(sc.getName());
			tc.setParentId(sc.getParentCid());
			list.add(tc);
			categoryMap.put(sc.getCid(), tc);
		}

		for (SellerCat sc : categoryList) {
			if (sc.getParentCid() != 0) {
				TaobaoCategory tc = categoryMap.get(sc.getParentCid());
				tc.setHasChild(true);
				tc.getChildren().add(categoryMap.get(sc.getCid()));
			}
		}

		List<TaobaoCategory> resultList = new ArrayList<TaobaoCategory>();
		for (TaobaoCategory tc : list) {
			if (tc.getParentId().equals(parentId)) {
				resultList.add(tc);
			}
		}
		request.setAttribute("categoryList", resultList);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/shopCategory";
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
		req.setFields("num_iid,title,nick,pic_url,price,click_url,shop_click_url,seller_credit_score,item_location,volume");
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

	private String getContent(HttpServletRequest request) throws Exception {
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
		return content;
	}

	@RequestMapping("/taobao/favoriteItem.xml")
	public String favoriteItemPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<TaobaokeItem> itemList = new ArrayList<TaobaokeItem>();
		String content = this.getContent(request);
		JSONObject json = JSONObject.fromObject(content);
		String result = "";
		if (json.containsKey("item")) {
			JSONObject item = json.getJSONObject("item");
			JSONArray items = item.getJSONArray("items");
			for (int i = 0; i < items.size(); i++) {
				String htmlContent = items.getString(i);
				htmlContent = URLDecoder.decode(htmlContent, "utf-8");
				result += htmlContent;
			}
		}
		PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
		factory.registerTag(new StrongTag());
		Parser parser = Parser.createParser(result, "utf-8");
		parser.setNodeFactory(factory);
		AndFilter idFilter = new AndFilter(new TagNameFilter("li"),
				new HasAttributeFilter("data-type", "FavItem"));
		NodeList nodeList = parser.parse(idFilter);
		for (int i = 0; i < nodeList.size(); i++) {
			TaobaokeItem ti = new TaobaokeItem();
			TagNode itemNode = ((TagNode) nodeList.elementAt(i));
			String id = itemNode.getAttribute("data-item-id");
			ti.setNumIid(Long.parseLong(id));
			for (int j = 0; j < itemNode.getChildren().size(); j++) {
				Node childNode = itemNode.getChildren().elementAt(j);
				if (childNode instanceof TagNode) {
					String className = ((TagNode) childNode)
							.getAttribute("class");
					if (className.equals("J_FavImgController")) {
						TagNode imageNode = (TagNode) getChildNode(childNode,
								new int[] { 0, 0 });
						ti.setPicUrl(imageNode.getAttribute("src"));
					} else if (className.equals("attribute")) {
						TagNode titleNode = (TagNode) getChildNode(childNode,
								new int[] { 0, 1 });
						ti.setTitle(titleNode.getAttribute("title"));
						TagNode priceNode = (TagNode) getChildNode(childNode,
								new int[] { 1, 0, 1, 1 });
						ti.setPrice(((CompositeTag) priceNode).getStringText());
						TagNode volumn = (TagNode) getChildNode(childNode,
								new int[] { 2, 0 });
						String stringVolumn = ((CompositeTag) volumn)
								.getStringText();
						if (stringVolumn.indexOf(":") != -1) {
							stringVolumn = stringVolumn
									.substring(stringVolumn.indexOf(":") + 1)
									.replaceAll("件", "").trim();
						}
						if (StringUtils.isNumeric(stringVolumn)) {
							ti.setVolume(Long.parseLong(stringVolumn));
						}
					}
				}
			}

			itemList.add(ti);
		}
		request.setAttribute("itemList", itemList);

		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/favorite";
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

	private Node getChildNode(Node node, int[] indexs) {
		Node r = node;
		try {
			for (int i = 0; i < indexs.length; i++) {
				int index = -1;
				for (int j = 0; j < r.getChildren().size(); j++) {
					Node n = r.getChildren().elementAt(j);
					if (n instanceof TagNode) {
						index++;
						if (index == indexs[i]) {
							r = n;
							break;
						}
					}
				}
				if (index != indexs[i]) {
					throw new RuntimeException("Can't find child:" + indexs[i]);
				}
			}
			return r;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}

	}
}
