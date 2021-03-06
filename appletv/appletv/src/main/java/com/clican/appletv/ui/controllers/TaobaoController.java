package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import com.clican.appletv.core.service.taobao.TaobaoClientImpl;
import com.clican.appletv.core.service.taobao.model.TaobaoAccessToken;
import com.clican.appletv.core.service.taobao.model.TaobaoAddress;
import com.clican.appletv.core.service.taobao.model.TaobaoCategory;
import com.clican.appletv.core.service.taobao.model.TaobaoConfirmOrder;
import com.clican.appletv.core.service.taobao.model.TaobaoFare;
import com.clican.appletv.core.service.taobao.model.TaobaoLoveTag;
import com.clican.appletv.core.service.taobao.model.TaobaoOrderByShop;
import com.clican.appletv.core.service.taobao.model.TaobaoSkuCache;
import com.clican.appletv.core.service.taobao.model.TaobaoSkuPromotionWrap;
import com.clican.appletv.core.service.taobao.model.TaobaoSkuValue;
import com.clican.appletv.ext.htmlparser.StrongTag;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemImg;
import com.taobao.api.domain.SellerCat;
import com.taobao.api.domain.Shop;
import com.taobao.api.domain.Sku;
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.internal.util.codec.Base64;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.ItemsListGetRequest;
import com.taobao.api.request.SellercatsListGetRequest;
import com.taobao.api.request.ShopGetRequest;
import com.taobao.api.request.TaobaokeItemsGetRequest;
import com.taobao.api.request.TaobaokeItemsRelateGetRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.ItemsListGetResponse;
import com.taobao.api.response.SellercatsListGetResponse;
import com.taobao.api.response.ShopGetResponse;
import com.taobao.api.response.TaobaokeItemsGetResponse;
import com.taobao.api.response.TaobaokeItemsRelateGetResponse;

@Controller
public class TaobaoController {

	private final static Log log = LogFactory.getLog(TaobaoController.class);
	public final static String TAOBAO_TOKEN_NAME = "taobaoAccessToken";
	public final static String TAOBAO_HTML_TOKEN_NAME = "taobaoHtmlToken";
	public final static String TAOBAO_HTML_TID_NAME = "taobaoHtmlTid";
	public final static String TAOBAO_USER_ID_NAME = "taobaoUserId";
	public final static String TAOBAO_SELLER_CATEGORY_LIST = "taobaoSellerCategoryList";
	public final static String TAOBAO_CONFIRM_ORDER = "tco";
	public final static String TAOBAO_SKU_NAME = "taobaoSkuName";
	public final static String TAOBAO_PROMOTION_NAME = "taobaoPromotionName";

	@Autowired
	private com.clican.appletv.core.service.taobao.TaobaoClient taobaoClient;

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

	@RequestMapping("/taobao/loginWithTokenAndTid.do")
	public void loginWithTokenAndTid(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String content = this.getContent(request);
			int start = content.indexOf("tid=");
			if (start == -1) {
				response.getOutputStream().write("failure".getBytes());
				if (log.isDebugEnabled()) {
					log.debug("Taobao user login failure");
				}
				return;
			}
			start = start + 4;
			int end = content.indexOf("&", start);
			String tid = content.substring(start, end);
			Parser parser = Parser.createParser(content, "utf-8");
			AndFilter tokenFilter = new AndFilter(new TagNameFilter("input"),
					new HasAttributeFilter("name", "_tb_token_"));

			NodeList list = parser.parse(tokenFilter);
			String token = null;

			if (list.size() > 0) {
				TagNode node = (TagNode) list.elementAt(0);
				token = node.getAttribute("value");
			}

			if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(tid)) {
				if (log.isDebugEnabled()) {
					log.debug("Taobao user login successfully sessionid:"
							+ request.getSession().getId() + ", with token:"
							+ token + ",tid:" + tid);
				}
				request.getSession()
						.setAttribute(TAOBAO_HTML_TOKEN_NAME, token);
				request.getSession().setAttribute(TAOBAO_HTML_TID_NAME, tid);
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

	@RequestMapping("/taobao/index.xml")
	public String indexPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access taobao index");
		}
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/index";
	}

	@RequestMapping("/taobao/getTokenAndTid.do")
	public void getTokenAndTid(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String token = (String) request.getSession().getAttribute(
				TAOBAO_HTML_TOKEN_NAME);
		String tid = (String) request.getSession().getAttribute(
				TAOBAO_HTML_TID_NAME);
		if (log.isDebugEnabled()) {
			log.debug("get token from session");
		}
		if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(tid)) {
			String content = "{\"token\":\"" + token + "\",\"tid\":\"" + tid
					+ "\"}";
			log.debug(content);
			response.getOutputStream().write(content.getBytes());
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
		if (shop == null) {
			return "taobao/noresult";
		}
		Long sid = shop.getSid();
		request.setAttribute("shop", shop);
		String shopUrl = "http://shop" + sid + ".taobao.com";
		String content = ((TaobaoClientImpl) taobaoClient).httpGet(shopUrl);

		int start = content.indexOf("userid=") + "userid=".length();
		int end = content.indexOf(";", start);
		Long sellerId = Long.parseLong(content.substring(start, end).trim());

		request.setAttribute("sellerId", sellerId);
		request.setAttribute("shopId", sid);
		request.setAttribute("nick", URLEncoder.encode(nick, "utf-8"));
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
		itemReq.setFields("num_iid,title,nick,pic_url,price,click_url,shop_click_url,item_location,volume");
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
		Shop shop = shopResp.getShop();
		request.setAttribute("shop", shop);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/shopDetail";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/taobao/shopCategory.xml")
	public String shopCategoryPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "nick", required = false) String nick,
			@RequestParam(value = "parentId", required = false) Long parentId,
			@RequestParam(value = "shopId", required = false) Long shopId)
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
			request.getSession().setAttribute(TAOBAO_SELLER_CATEGORY_LIST,
					categoryList);
		}

		Map<Long, TaobaoCategory> categoryMap = new HashMap<Long, TaobaoCategory>();
		List<TaobaoCategory> list = new ArrayList<TaobaoCategory>();
		for (SellerCat sc : categoryList) {
			TaobaoCategory tc = new TaobaoCategory();
			tc.setId(sc.getCid());
			tc.setPicUrl(sc.getPicUrl());
			tc.setTitle(sc.getName());
			tc.setBase64Title(new String(Base64.encodeBase64(sc.getName()
					.getBytes("gb2312"))));
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
		request.setAttribute("shopId", shopId);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/shopCategory";
	}

	@RequestMapping("/taobao/shopCategoryItemList.xml")
	public String shopCategoryItemListPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "itemIds", required = false) String itemIds,
			@RequestParam(value = "scid", required = false) Long scid)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access shop category item:" + itemIds);
		}
		if (StringUtils.isEmpty(itemIds)) {
			return "taobao/noresult";
		}
		if (itemIds.endsWith(",")) {
			itemIds = itemIds.substring(0, itemIds.length() - 1);
		}
		String[] itemIdArray = itemIds.split(",");
		int endIndex = 20;
		if (itemIdArray.length < 20) {
			endIndex = itemIdArray.length;
		}

		String ids = StringUtils.join(itemIdArray, ",", 0, endIndex);
		ItemsListGetRequest itemsReq = new ItemsListGetRequest();
		itemsReq.setFields("num_iid,title,nick,pic_url,price,click_url,item_location,volume,seller_cids");
		itemsReq.setNumIids(ids);
		ItemsListGetResponse itemsResp = taobaoRestClient.execute(itemsReq);
		List<Item> itemList = itemsResp.getItems();
		List<Item> result = new ArrayList<Item>();
		for (Item item : itemList) {
			if (item.getSellerCids().contains(scid.toString())) {
				result.add(item);
			}
		}
		request.setAttribute("itemList", result);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/shopCategoryItemList";
	}

	@RequestMapping("/taobao/itemList.xml")
	public String itemListPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "cid", required = false) Long cid,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", required = false) Long page,
			@RequestParam(value = "sort", required = false) String sort)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access item list for cid:" + cid);
		}

		if (page == null) {
			page = 1L;
		}
		TaobaokeItemsGetRequest req = new TaobaokeItemsGetRequest();
		req.setFields("num_iid,title,nick,pic_url,price,click_url,shop_click_url,seller_credit_score,item_location,volume");
		if (cid != null) {
			req.setCid(cid);
		}

		if (StringUtils.isNotEmpty(keyword)) {
			req.setKeyword(keyword);
		}

		req.setPageNo(page);
		req.setPageSize(40L);
		if (StringUtils.isEmpty(sort)) {
			sort = "default";
		}
		req.setSort(sort);
		TaobaokeItemsGetResponse resp = taobaoRestClient.execute(req);
		long totalPage = (resp.getTotalResults() - 1) / req.getPageSize();
		if (totalPage > 10) {
			totalPage = 10;
		}
		List<TaobaokeItem> itemList = resp.getTaobaokeItems();

		long begin = 1, end = 1;
		if (page < totalPage - 10) {
			begin = page;
			end = page + 9;
		} else {
			end = totalPage;
			begin = totalPage - 9;
			if (begin < 1) {
				begin = 1;
			}
		}
		String pagiurl = springProperty.getSystemServerUrl()
				+ "/ctl/taobao/itemList.xml?sort=" + sort;
		String sorturl = springProperty.getSystemServerUrl()
				+ "/ctl/taobao/itemList.xml?1=1";
		if (cid != null) {
			pagiurl = pagiurl + "&amp;cid=" + cid;
			sorturl = sorturl + "&amp;cid=" + cid;
		}
		if (StringUtils.isNotEmpty(keyword)) {
			pagiurl = pagiurl + "&amp;keyword="
					+ URLEncoder.encode(keyword, "utf-8");
			sorturl = sorturl + "&amp;keyword="
					+ URLEncoder.encode(keyword, "utf-8");
		}
		request.setAttribute("sort", sort);
		request.setAttribute("pagiurl", pagiurl);
		request.setAttribute("sorturl", sorturl);
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
						NodeList priceNodeList = new NodeList();
						childNode.collectInto(priceNodeList, new TagNameFilter(
								"strong"));
						if (priceNodeList.size() > 0) {
							CompositeTag priceNode = (CompositeTag) priceNodeList
									.elementAt(0);
							ti.setPrice(priceNode.getStringText());
						}
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
		if (itemList.size() == 0) {
			return "taobao/noresult";
		} else {
			return "taobao/favoriteItem";
		}
	}

	@RequestMapping("/taobao/favoriteShop.xml")
	public String favoriteShopPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Shop> shopList = new ArrayList<Shop>();
		String htmlContent = this.getContent(request);

		PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
		factory.registerTag(new StrongTag());
		Parser parser = Parser.createParser(htmlContent, "utf-8");
		parser.setNodeFactory(factory);
		AndFilter idFilter = new AndFilter(new TagNameFilter("li"),
				new HasAttributeFilter("data-type", "FavItem"));
		NodeList nodeList = parser.parse(idFilter);
		for (int i = 0; i < nodeList.size(); i++) {
			TagNode itemNode = ((TagNode) nodeList.elementAt(i));
			Shop shop = new Shop();
			for (int j = 0; j < itemNode.getChildren().size(); j++) {
				Node childNode = itemNode.getChildren().elementAt(j);
				if (childNode instanceof TagNode) {
					String className = ((TagNode) childNode)
							.getAttribute("class");
					if (className.contains("thumbnail-private")) {
						TagNode imageNode = (TagNode) getChildNode(childNode,
								new int[] { 0, 0 });
						shop.setPicPath(imageNode
								.getAttribute("data-ks-lazyload"));
					} else if (className
							.contains("J_NewAucBox J_SimilarItemsBox")) {
						CompositeTag titleNode = (CompositeTag) getChildNode(
								childNode, new int[] { 0, 0, 0 });
						shop.setTitle(titleNode.getStringText().trim());
						shop.setNick(shop.getTitle());
					}
				}
			}
			shopList.add(shop);
		}
		request.setAttribute("shopList", shopList);

		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		if (shopList.size() == 0) {
			return "taobao/noresult";
		} else {
			return "taobao/favoriteShop";
		}

	}

	@RequestMapping("/taobao/item.xml")
	public String itemPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "itemId", required = false) Long itemId,
			@RequestParam(value = "volume", required = false) Long volume,
			@RequestParam(value = "microscopedata", required = false) String microscopedata)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access item :" + itemId);
		}

		String sellerId = null;

		if (StringUtils.isNotEmpty(microscopedata)) {
			for (String pair : microscopedata.split(";")) {
				String[] p = pair.split("=");
				if (p[0].trim().equals("userid")) {
					sellerId = p[1].trim();
				}
			}
		}
		ItemGetRequest req = new ItemGetRequest();
		req.setFields("detail_url,num_iid,num,title,nick,desc,location,price,post_fee,express_fee,ems_fee,item_img.url,videos,pic_url,stuff_status,sku,property_alias,props");
		req.setNumIid(itemId);
		ItemGetResponse resp = taobaoRestClient.execute(req);
		Item item = resp.getItem();

		if (log.isDebugEnabled()) {
			log.debug("detail url:" + item.getDetailUrl());
		}

		TaobaokeItemsRelateGetRequest req3 = new TaobaokeItemsRelateGetRequest();
		req3.setRelateType(1L);
		req3.setNumIid(itemId);
		req3.setFields("num_iid,title,pic_url,volume,click_url");
		TaobaokeItemsRelateGetResponse resp3 = taobaoRestClient.execute(req3);
		TaobaoSkuPromotionWrap promotion = null;
		try{
			Double price = Double.parseDouble(item.getPrice());
			price = price * 100;
			String promotionUrl = "http://ajax.tbcdn.cn/json/promotionListn.htm?price="
					+ price.intValue()
					+ "&itemId="
					+ itemId
					+ "&sellerId="
					+ sellerId;
			if (StringUtils.isNotEmpty(promotionUrl)) {
				promotion = taobaoClient.getPromotion(item, promotionUrl);
			}
		}catch(Exception e){
			log.error("",e);
		}
		

		String imageUrls = "";
		if (item.getItemImgs() != null && item.getItemImgs().size() > 0) {
			for (ItemImg ii : item.getItemImgs()) {
				imageUrls += ii.getUrl() + ",";
			}
		}
		if (imageUrls.endsWith(",")) {
			imageUrls = imageUrls.substring(0, imageUrls.length() - 1);
		}

		if (StringUtils.isNotEmpty(imageUrls)) {
			request.setAttribute("imageUrls", imageUrls);
		}
		List<TaobaokeItem> relatedItemList = resp3.getTaobaokeItems();
		request.setAttribute("relatedItemList", relatedItemList);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("promotion", promotion);

		item.setVolume(volume);
		TaobaoSkuCache tks = this.getSkuMap(item);
		tks.setTaobaoSkuPromotionWrap(promotion);
		request.getSession().setAttribute(TAOBAO_SKU_NAME, tks);
		request.setAttribute("item", item);
		request.setAttribute("sellerId",sellerId);
		return "taobao/item";

	}

	@SuppressWarnings({ "unchecked" })
	private TaobaoSkuCache getSkuMap(Item item) {
		TaobaoSkuCache cache = new TaobaoSkuCache();
		List<String> skuLabelList = new ArrayList<String>();
		Map<String, Set<TaobaoSkuValue>> skuLabelValueMap = new HashMap<String, Set<TaobaoSkuValue>>();
		Map<String, Object> skuRelationMap = new HashMap<String, Object>();
		Map<String, Sku> skuMap = new HashMap<String, Sku>();
		cache.setSkuLabelList(skuLabelList);
		cache.setSkuLabelValueMap(skuLabelValueMap);
		cache.setItem(item);
		cache.setSkuRelationMap(skuRelationMap);
		cache.setSkuMap(skuMap);
		String propertyAlias = item.getPropertyAlias();
		Map<String, String> aliaMap = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(propertyAlias)) {
			String[] alias = propertyAlias.split(";");
			for (String alia : alias) {
				int index = alia.lastIndexOf(":");
				String key = alia.substring(0, index);
				String value = alia.substring(index + 1);
				aliaMap.put(key, value);
			}
		}
		if (item.getSkus() != null) {
			for (Sku sku : item.getSkus()) {
				if (sku.getQuantity() == 0) {
					continue;
				}
				skuMap.put(sku.getProperties(), sku);
				
				String[] skuProps = sku.getProperties().split(";");
				String[] skuPropNames = sku.getPropertiesName().split(";");
				Map<String, Object> tempMap = null;
				for (int i = 0; i < skuProps.length; i++) {
					if (i == 0) {
						tempMap = skuRelationMap;
					}
					String skuLabelValue = skuProps[i];
					if (!tempMap.containsKey(skuLabelValue)) {
						Map<String, Object> map = new HashMap<String, Object>();
						tempMap.put(skuLabelValue, map);
						tempMap = map;
					} else {
						tempMap = (Map<String, Object>) tempMap
								.get(skuLabelValue);
					}

					String skuLabel = skuPropNames[i].replace(skuLabelValue
							+ ":", "");
					String skuLabelLabel = skuLabel.split(":")[1];
					if (aliaMap.containsKey(skuLabelValue)) {
						skuLabelLabel = aliaMap.get(skuLabelValue);
					}
					skuLabel = skuLabel.split(":")[0];
					if (!skuLabelList.contains(skuLabel)) {
						skuLabelList.add(skuLabel);
						skuLabelValueMap.put(skuLabel,
								new TreeSet<TaobaoSkuValue>());
					}
					TaobaoSkuValue tsv = new TaobaoSkuValue();
					tsv.setLabel(skuLabelLabel);
					tsv.setValue(skuLabelValue);
					skuLabelValueMap.get(skuLabel).add(tsv);
				}
			}
		}
		return cache;
	}

	@RequestMapping("/taobao/itemSelect.xml")
	public String itemSelectPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "itemId", required = false) Long itemId,
			@RequestParam(value = "sellerId", required = false) String sellerId,
			@RequestParam(value = "selectedValue", required = false) String selectedValue)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access item :" + itemId);
		}
		TaobaoSkuCache tsc = (TaobaoSkuCache) request.getSession()
				.getAttribute(TAOBAO_SKU_NAME);
		if (tsc == null || !tsc.getItem().getNumIid().equals(itemId)) {
			ItemGetRequest req = new ItemGetRequest();
			req.setFields("detail_url,num_iid,num,title,nick,desc,location,price,post_fee,express_fee,ems_fee,item_img.url,videos,pic_url,stuff_status,sku,property_alias,props");
			req.setNumIid(itemId);
			ItemGetResponse resp = taobaoRestClient.execute(req);
			Item item = resp.getItem();
			TaobaoSkuPromotionWrap promotion = null;
			try{
				Double price = Double.parseDouble(item.getPrice());
				price = price * 100;
				String promotionUrl = "http://ajax.tbcdn.cn/json/promotionListn.htm?price="
						+ price.intValue()
						+ "&itemId="
						+ itemId
						+ "&sellerId="
						+ sellerId;
				if (StringUtils.isNotEmpty(promotionUrl)) {
					promotion = taobaoClient.getPromotion(item, promotionUrl);
				}
			}catch(Exception e){
				log.error("",e);
			}
			tsc = this.getSkuMap(item);
			tsc.setTaobaoSkuPromotionWrap(promotion);
			request.getSession().setAttribute(TAOBAO_SKU_NAME, tsc);
		}

		tsc.updateSelectedValues(selectedValue);
		request.setAttribute("tsc", tsc);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/itemSelect";

	}

	@RequestMapping("/taobao/loveTag.xml")
	public String loveTagPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "gender", required = false) String gender)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access love page gender:" + gender);
		}
		if (StringUtils.isEmpty(gender)) {
			gender = "woman";
		}
		List<TaobaoLoveTag> tagList = taobaoClient.getTaobaoLoveTags().get(
				gender);

		request.setAttribute("gender", gender);
		request.setAttribute("tagList", tagList);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "taobao/loveTag";

	}

	@RequestMapping("/taobao/myCart.do")
	public void myCart(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access my cart");
		}
		String htmlContent = this.getContent(request);
		Parser itemParser = Parser.createParser(htmlContent, "utf-8");
		AndFilter idFilter = new AndFilter(new TagNameFilter("tr"),
				new HasAttributeFilter("data-cartid"));
		AndFilter amountFilter = new AndFilter(new TagNameFilter("input"),
				new HasAttributeFilter("class", "text text-amount"));
		NodeList itemNodeList = itemParser.parse(idFilter);
		if (itemNodeList.size() == 0) {
			response.setCharacterEncoding("text/plain;charset=utf-8");
			response.getOutputStream().write("noresult".getBytes("utf-8"));
			response.getOutputStream().flush();
		} else {
			String result = "buyer_from=cart&serviceCartIds&item=";
			for (int i = 0; i < itemNodeList.size(); i++) {
				TagNode itemNode = (TagNode) itemNodeList.elementAt(i);
				String cardid = itemNode.getAttribute("data-cartid");
				String itemid = itemNode.getAttribute("data-itemid");
				String skuid = itemNode.getAttribute("data-skuid");
				if (StringUtils.isEmpty(skuid)) {
					skuid = "0";
				}
				NodeList amountNodeList = new NodeList();
				itemNode.collectInto(amountNodeList, amountFilter);
				if (amountNodeList.size() > 0) {
					String amount = ((TagNode) amountNodeList.elementAt(0))
							.getAttribute("value");
					result = result + cardid + "_" + itemid + "_" + amount
							+ "_" + skuid + "_0_0_0,";
				} else {
					result = result + cardid + "_" + itemid + "_1_" + skuid
							+ "_0_0_0,";
				}

			}
			if (result.endsWith(",")) {
				result = result.substring(0, result.length() - 1);
			}
			if (log.isDebugEnabled()) {
				log.debug("my cart content:" + result);
			}
			response.setCharacterEncoding("text/plain;charset=utf-8");
			response.getOutputStream().write(result.getBytes("utf-8"));
			response.getOutputStream().flush();
		}
	}

	@RequestMapping("/taobao/confirmOrder.xml")
	public String confirmOrderPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access confirm order sessionid:"
					+ request.getSession().getId());
		}
		String content = this.getContent(request);
		JSONObject json = JSONObject.fromObject(content);
		String deviceId = json.getString("deviceId");
		String htmlContent = json.getString("htmlContent");
		TaobaoConfirmOrder tco = taobaoClient.getConfirmOrder(htmlContent);
		if (tco.getShopList().size() == 0) {
			request.setAttribute("tco", tco);
			request.setAttribute("title", "购物车内空空如也");
			return "taobao/noresult";
		} else {
			taobaoClient.cacheConfirOrderImage(deviceId,
					tco.getConfirmOrderImage());
			request.setAttribute("serverurl",
					springProperty.getSystemServerUrl());
			request.getSession().setAttribute(TAOBAO_CONFIRM_ORDER, tco);
			return "taobao/confirmOrder";
		}
	}

	@RequestMapping("/taobao/getConfirmOrder.png")
	public void getConfirmOrderPng(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "deviceId", required = true) String deviceId)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("access confirm order png");
		}
		byte[] image = taobaoClient.getConfirOrderImage(deviceId);
		response.setContentType("image/png");
		response.setContentLength(image.length);
		response.getOutputStream().write(image);
	}

	@RequestMapping("/taobao/changeAddr.xml")
	public String changeAddrPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String content = this.getContent(request);
		JSONObject jsonObj = JSONObject.fromObject(content);
		Long addrId = jsonObj.getLong("addrId");
		String fareResponse = jsonObj.getString("fareResponse");
		TaobaoConfirmOrder tco = (TaobaoConfirmOrder) request.getSession()
				.getAttribute(TAOBAO_CONFIRM_ORDER);
		if (tco == null) {
			request.setAttribute("title", "结算页面过期请重新进入");
			return "taobao/noresult";
		} else {
			JSONObject json = JSONObject.fromObject(fareResponse);
			for (TaobaoOrderByShop shop : tco.getShopList()) {
				JSONArray array = json.getJSONArray(shop.getOutOrderId());
				List<TaobaoFare> fareList = new ArrayList<TaobaoFare>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject fareJson = array.getJSONObject(i);
					TaobaoFare fare = new TaobaoFare();
					fare.setId(fareJson.getString("id"));
					fare.setLabel(fareJson.getString("message"));
					fareList.add(fare);
				}
				shop.setFareList(fareList);
			}

			for (TaobaoAddress addr : tco.getAddrList()) {
				if (addr.getAddrId().equals(addrId)) {
					String addrParams = addr.getAddrParams();
					String[] params = addrParams.split("^^");
					for (String param : params) {
						int index = param.indexOf("=");
						if (index > 0) {
							String name = param.substring(0, index);
							String value = "";
							if (index < param.length()) {
								value = param.substring(index + 1);
							}
							if (name.equals("id")) {
								tco.getForms().put("addressId", value);
							} else if (name.equals("address")) {
								tco.getForms().put("deliverAddr", value);
							} else if (name.equals("postCode")) {
								tco.getForms().put("postCode", value);
							} else if (name.equals("mobile")) {
								tco.getForms().put("deliverMobile", value);
							} else if (name.equals("addressee")) {
								tco.getForms().put("deliverName", value);
							} else if (name.equals("phone")) {
								tco.getForms().put("deliverPhone", value);
							} else if (name.equals("areaCode")) {
								tco.getForms().put("divisionCode", value);
							}
						}
					}
				}
			}
			request.setAttribute("serverurl",
					springProperty.getSystemServerUrl());
			request.getSession().setAttribute(TAOBAO_CONFIRM_ORDER, tco);
			return "taobao/confirmOrder";
		}

	}

	@RequestMapping("/taobao/getFareRequest.do")
	public void getFareRequest(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "addrId", required = false) Long addrId)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("get fare request sessionid:"
					+ request.getSession().getId() + ",addrId:" + addrId);
		}
		TaobaoConfirmOrder tco = (TaobaoConfirmOrder) request.getSession()
				.getAttribute(TAOBAO_CONFIRM_ORDER);
		if (tco == null) {
			log.warn("Can't find tco from session");
			response.getOutputStream().write("error".getBytes());
		} else {
			for (TaobaoAddress addr : tco.getAddrList()) {
				if (addr.getAddrId().equals(addrId)) {
					response.getOutputStream().write(
							addr.getFareRequest().getBytes("utf-8"));
					tco.setSelectedAddrId(addrId);
					return;
				}
			}
			log.warn("Can't find address by addrId");
			response.getOutputStream().write("error".getBytes());
		}
	}

	@RequestMapping("/taobao/getSubmitRequest.do")
	public void getFareRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("get submit request");
		}
		TaobaoConfirmOrder tco = (TaobaoConfirmOrder) request.getSession()
				.getAttribute(TAOBAO_CONFIRM_ORDER);
		if (tco == null) {
			log.warn("Can't find tco from session");
			response.getOutputStream().write("error".getBytes());
		} else {
			Map<String, String> forms = tco.getForms();
			String formStr = "";
			String jsonContent = "[";
			for (String key : forms.keySet()) {
				String value = forms.get(key);
				if (value == null) {
					value = "";
				}
				jsonContent += "{\"name\":\"" + key + "\",\"value\":\"" + value
						+ "\"},";
				formStr += key + ":" + value + "\n";
			}
			for (TaobaoOrderByShop shop : tco.getShopList()) {
				jsonContent += "{\"name\":\"" + shop.getFareName()
						+ "\",\"value\":\"" + shop.getSelectedFareId() + "\"},";
				formStr += shop.getFareName() + ":" + shop.getSelectedFareId()
						+ "\n";
			}
			if (jsonContent.endsWith(",")) {
				jsonContent = jsonContent
						.substring(0, jsonContent.length() - 1);
			}
			jsonContent += "]";
			if (log.isDebugEnabled()) {
				log.debug("submitRequest\n" + jsonContent);
			}
			response.getOutputStream().write(jsonContent.getBytes("utf-8"));
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
