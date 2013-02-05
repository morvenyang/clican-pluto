package com.clican.appletv.core.service.taobao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.util.NodeList;

import com.clican.appletv.core.service.baibian.model.BaseClient;
import com.clican.appletv.core.service.taobao.model.TaobaoAccessToken;
import com.clican.appletv.core.service.taobao.model.TaobaoAddress;
import com.clican.appletv.core.service.taobao.model.TaobaoCategory;
import com.clican.appletv.core.service.taobao.model.TaobaoConfirmOrder;
import com.clican.appletv.core.service.taobao.model.TaobaoFare;
import com.clican.appletv.core.service.taobao.model.TaobaoLove;
import com.clican.appletv.core.service.taobao.model.TaobaoLoveTag;
import com.clican.appletv.core.service.taobao.model.TaobaoOrderByItem;
import com.clican.appletv.core.service.taobao.model.TaobaoOrderByShop;
import com.clican.appletv.ext.htmlparser.EmTag;
import com.clican.appletv.ext.htmlparser.STag;
import com.clican.appletv.ext.htmlparser.StrongTag;
import com.clican.appletv.ext.htmlparser.TbodyTag;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.response.ItemcatsGetResponse;

public class TaobaoClientImpl extends BaseClient implements TaobaoClient {

	private Map<Long, TaobaoCategory> taobaoCategoryMap = new HashMap<Long, TaobaoCategory>();

	private com.taobao.api.TaobaoClient taobaoRestClient;

	private List<TaobaoCategory> taobaoTopCategoryList;

	public void setTaobaoRestClient(com.taobao.api.TaobaoClient taobaoRestClient) {
		this.taobaoRestClient = taobaoRestClient;
	}

	public void setTaobaoTopCategoryList(
			List<TaobaoCategory> taobaoTopCategoryList) {
		this.taobaoTopCategoryList = taobaoTopCategoryList;
	}

	@Override
	public List<TaobaoLove> queryTaobaoLoves(Long tagId) {
		this.checkCache();
		String url = springProperty.getTaobaoLoveApi() + "?tagid=" + tagId
				+ "&pagenum=1";
		String json;
		if (shortCacheMap.containsKey(url)) {
			json = shortCacheMap.get(url);
		} else {
			if (shortCacheMap.containsKey(url)) {
				json = shortCacheMap.get(url);
			} else {
				json = this.httpGet(url);
				shortCacheMap.put(url, json);
			}
		}
		List<TaobaoLove> result = new ArrayList<TaobaoLove>();
		JSONArray array = JSONObject.fromObject(json).getJSONArray("itemList");
		for (int i = 0; i < array.size(); i++) {
			TaobaoLove taobaoLove = new TaobaoLove();
			JSONObject item = array.getJSONObject(i);
			taobaoLove.setPicUrl(item.getString("picUrl"));
			taobaoLove.setItemId(item.getLong("itemId"));
			taobaoLove.setSellerNick(item.getString("sellerNick"));
			String title = "买家 " + item.getString("buyerNick") + " "
					+ item.getString("operateType");
			taobaoLove.setTitle(title);
			taobaoLove.setOperateTime(item.getString("operateTime"));
			result.add(taobaoLove);
		}
		return result;
	}

	@Override
	public Map<String, List<TaobaoLoveTag>> getTaobaoLoveTags() {
		String url = springProperty.getTaobaoLoveTagApi();
		String json;
		if (cacheMap.containsKey(url)) {
			json = cacheMap.get(url);
		} else {
			synchronized (this) {
				if (cacheMap.containsKey(url)) {
					json = cacheMap.get(url);
				} else {
					json = this.httpGet(url);
					cacheMap.put(url, json);
				}
			}
		}
		JSONArray array = JSONArray.fromObject(json);
		Map<String, List<TaobaoLoveTag>> result = new HashMap<String, List<TaobaoLoveTag>>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String groupCode = obj.getString("groupCode");
			if (groupCode.equals("man") || groupCode.equals("woman")) {
				List<TaobaoLoveTag> tagList = new ArrayList<TaobaoLoveTag>();
				result.put(groupCode, tagList);
				JSONArray tags = obj.getJSONArray("tags");
				for (int j = 0; j < tags.size(); j++) {
					TaobaoLoveTag tag = new TaobaoLoveTag();
					String name = tags.getJSONObject(j).getString("name");
					Long id = tags.getJSONObject(j).getLong("id");
					tag.setId(id);
					tag.setName(name);
					tagList.add(tag);
				}
			}
		}
		return result;
	}

	@Override
	public TaobaoAccessToken getAccessToken(String code) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("grant_type", "authorization_code");
		param.put("code", code);
		param.put("client_id", springProperty.getTaobaoAppKey());
		param.put("client_secret", springProperty.getTaobaoAppSercret());
		param.put("redirect_uri", springProperty.getTaobaoRedirectUrl());
		try {
			String responseJson = WebUtils.doPost(
					springProperty.getTaobaoAuthorizeBaseUrl() + "/token",
					param, 3000, 3000);
			JSONObject json = JSONObject.fromObject(responseJson);
			TaobaoAccessToken taobaoAccessToken = new TaobaoAccessToken();
			taobaoAccessToken.setAccessToken(json.getString("access_token"));
			taobaoAccessToken.setRefreshToken(json.getString("refresh_token"));
			taobaoAccessToken.setUserId(json.getString("taobao_user_id"));
			taobaoAccessToken.setUserNick(json.getString("taobao_user_nick"));
			return taobaoAccessToken;
		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public void init() {
		try {
			File taobaoCategoryJsonFile = new File(
					springProperty.getTaobaoCategoryJsonFile());
			if (taobaoCategoryJsonFile.exists()) {
				String content = FileUtils.readFileToString(
						taobaoCategoryJsonFile, "utf-8");
				Map<String, Class> classMap = new HashMap<String, Class>();
				classMap.put("children", TaobaoCategory.class);
				List<TaobaoCategory> result = (List<TaobaoCategory>) JSONArray
						.toList(JSONArray.fromObject(content),
								TaobaoCategory.class, classMap);
				taobaoTopCategoryList = result;
			} else {
				ItemcatsGetRequest req = new ItemcatsGetRequest();
				req.setFields("cid,parent_cid,name,is_parent");
				req.setParentCid(0L);
				ItemcatsGetResponse response = taobaoRestClient.execute(req);
				List<ItemCat> cats = response.getItemCats();
				Map<Long, ItemCat> catMap = new HashMap<Long, ItemCat>();
				for (ItemCat ic : cats) {
					catMap.put(ic.getCid(), ic);
				}
				for (TaobaoCategory tc : taobaoTopCategoryList) {
					for (String cid : tc.getChildrenCids()) {
						TaobaoCategory child = new TaobaoCategory();
						ItemCat ic = catMap.get(Long.parseLong(cid));
						child.setTitle(ic.getName());
						child.setId(ic.getCid());
						tc.getChildren().add(child);
						req.setParentCid(ic.getCid());
						ItemcatsGetResponse resp = taobaoRestClient
								.execute(req);
						List<ItemCat> itemCats = resp.getItemCats();
						if (itemCats != null) {
							for (ItemCat i : itemCats) {
								TaobaoCategory c = new TaobaoCategory();
								c.setTitle(i.getName());
								c.setId(i.getCid());
								child.getChildren().add(c);
							}
						}
					}
				}
				String content = JSONArray.fromObject(taobaoTopCategoryList)
						.toString();
				FileUtils.write(taobaoCategoryJsonFile, content, "utf-8");
			}
			for (TaobaoCategory tc1 : taobaoTopCategoryList) {
				this.taobaoCategoryMap.put(tc1.getId(), tc1);
				if (tc1.getChildren() != null && tc1.getChildren().size() > 0) {
					for (TaobaoCategory tc2 : tc1.getChildren()) {
						this.taobaoCategoryMap.put(tc2.getId(), tc2);
						if (tc2.getChildren() != null
								&& tc2.getChildren().size() > 0) {
							for (TaobaoCategory tc3 : tc2.getChildren()) {
								this.taobaoCategoryMap.put(tc3.getId(), tc3);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

	@Override
	public List<Long> getItemsBySellerCategory(Long shopId, Long scid,
			String scname) {
		String url = "http://shop" + shopId + ".taobao.com/search.htm?scid="
				+ scid + "&scname=" + scname
				+ "&checkedRange=true&queryType=cat";
		String htmlContent = this.httpGet(url);
		Parser parser = Parser.createParser(htmlContent, "utf-8");
		List<Long> result = new ArrayList<Long>();
		Pattern pattern = Pattern.compile(".*id=(\\p{Digit}*).*");
		try {
			NodeList nodeList = parser.parse(new LinkStringFilter("item.htm"));
			for (int i = 0; i < nodeList.size(); i++) {
				TagNode node = (TagNode) nodeList.elementAt(i);
				String href = node.getAttribute("href");
				Matcher matcher = pattern.matcher(href);
				if (matcher.matches()) {
					String id = matcher.group(1);
					result.add(Long.parseLong(id));
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return result;
	}

	@Override
	public boolean addFavorite(Integer itemtype, Integer isMall, Integer isLp,
			Integer isTaohua, Long id, String cookie, String token) {
		Map<String, String> nameValueMap = new HashMap<String, String>();
		nameValueMap.put("itemtype", "1");
		nameValueMap.put("isTmall", "1");
		nameValueMap.put("isLp", "");
		nameValueMap.put("isTaohua", "");
		nameValueMap.put("id", id.toString());
		nameValueMap.put("_tb_token_", token);
		Map<String, String> header = new HashMap<String, String>();
		header.put("Cookie", cookie);
		String url2 = "http://favorite.taobao.com/popup/add_collection.htm";
		this.httpPost(url2, null, nameValueMap,
				"application/x-www-form-urlencoded", "utf-8", header, null);
		return true;
	}

	@Override
	public List<TaobaoCategory> getTopCategories() {
		return this.taobaoTopCategoryList;
	}

	@Override
	public List<TaobaoCategory> getCategories(Long parentId) {
		TaobaoCategory tc = this.taobaoCategoryMap.get(parentId);
		if (tc != null) {
			return tc.getChildren();
		} else {
			return new ArrayList<TaobaoCategory>();
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

	@Override
	public TaobaoConfirmOrder getConfirmOrder(String htmlContent) {
		TaobaoConfirmOrder tco = new TaobaoConfirmOrder();
		List<TaobaoOrderByShop> shopList = new ArrayList<TaobaoOrderByShop>();
		List<TaobaoAddress> addrList = new ArrayList<TaobaoAddress>();
		tco.setAddrList(addrList);
		tco.setShopList(shopList);

		PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
		factory.registerTag(new StrongTag());
		factory.registerTag(new EmTag());
		factory.registerTag(new STag());
		factory.registerTag(new TbodyTag());
		Parser addrParser = Parser.createParser(htmlContent, "utf-8");
		Parser shopParser = Parser.createParser(htmlContent, "utf-8");
		try {
			addrParser.setNodeFactory(factory);
			shopParser.setNodeFactory(factory);
			AndFilter addrFilter = new AndFilter(new TagNameFilter("ul"),
					new HasAttributeFilter("id", "address-list"));

			NodeList addrListNode = addrParser.parse(addrFilter);
			if (addrListNode.size() > 0) {
				TagNode temp = (TagNode) addrListNode.elementAt(0);
				addrListNode = temp.getChildren();
				for (int i = 0; i < addrListNode.size(); i++) {
					Node addrNode = addrListNode.elementAt(i);
					if (addrNode instanceof Bullet) {
						Bullet bullet = (Bullet) addrNode;
						CompositeTag addrLabelNode = (CompositeTag) this
								.getChildNode(bullet, new int[] { 4 });
						String addrLabel = addrLabelNode.toString();
						addrLabel = addrLabel.replace("LABEL:", "").trim();
						TaobaoAddress addr = new TaobaoAddress();
						addr.setAddress(addrLabel);
						TagNode addrValueNode = (TagNode) this.getChildNode(
								bullet, new int[] { 3 });
						addr.setAddrId(Long.parseLong(addrValueNode
								.getAttribute("value")));
						String areaCode = addrValueNode
								.getAttribute("ah:params");
						if (areaCode != null) {
							int start = areaCode.indexOf("areaCode=");
							if (start > 0) {
								start = start + 9;
								int end = areaCode.indexOf("^^", start);
								if (end > 0) {
									areaCode = areaCode.substring(start, end);
								} else {
									areaCode = areaCode.substring(start);
								}
								addr.setAreaCode(areaCode);
							}
						}
						addrList.add(addr);
					}
				}
			}

			AndFilter shopFilter = new AndFilter(new TagNameFilter("tbody"),
					new HasAttributeFilter("data-outorderid"));

			NodeList orderByShopListNode = shopParser.parse(shopFilter);

			AndFilter sellerFilter = new AndFilter(new TagNameFilter("span"),
					new HasAttributeFilter("class", "seller"));
			AndFilter fareFilter = new AndFilter(new TagNameFilter("select"),
					new HasAttributeFilter("class", "J_Fare"));
			AndFilter itemFilter = new AndFilter(new TagNameFilter("tr"),
					new HasAttributeFilter("class", "item"));

			for (int i = 0; i < orderByShopListNode.size(); i++) {
				TaobaoOrderByShop shop = new TaobaoOrderByShop();
				shopList.add(shop);
				List<TaobaoFare> fareList = new ArrayList<TaobaoFare>();
				List<TaobaoOrderByItem> itemList = new ArrayList<TaobaoOrderByItem>();
				shop.setFareList(fareList);
				shop.setItemList(itemList);
				TagNode orderByShopNode = (TagNode) orderByShopListNode
						.elementAt(i);
				shop.setOutOrderId(orderByShopNode
						.getAttribute("data-outorderid"));
				shop.setPostMode(orderByShopNode.getAttribute("data-postMode"));
				NodeList sellerNodeList = new NodeList();
				orderByShopNode.collectInto(sellerNodeList, sellerFilter);
				if (sellerNodeList.size() > 0) {
					CompositeTag hrefNode = (CompositeTag) this.getChildNode(
							sellerNodeList.elementAt(0), new int[] { 0 });
					shop.setTitle(hrefNode.getStringText());
				}
				NodeList fareNodeList = new NodeList();
				orderByShopNode.collectInto(fareNodeList, fareFilter);
				if (fareNodeList.size() > 0) {
					Node fareSelectNode = fareNodeList.elementAt(0);
					for (int j = 0; j < fareSelectNode.getChildren().size(); j++) {
						Node fareNode = fareSelectNode.getChildren().elementAt(
								j);
						if (fareNode instanceof OptionTag) {
							TaobaoFare fare = new TaobaoFare();
							OptionTag fareOption = (OptionTag) fareNode;
							fare.setId(fareOption.getValue().trim());
							fare.setLabel(fareOption.getOptionText().trim());
							fareList.add(fare);
							if (fareList.size() == 1) {
								shop.setSelectedFareId(fare.getId());
							}
						}
					}
				}

				NodeList itemNodeList = new NodeList();
				orderByShopNode.collectInto(itemNodeList, itemFilter);
				for (int j = 0; j < itemNodeList.size(); j++) {
					TagNode itemNode = (TagNode) itemNodeList.elementAt(j);
					TaobaoOrderByItem item = new TaobaoOrderByItem();
					item.setDateId(itemNode.getAttribute("data-lineid"));
					TagNode hrefNode = (TagNode) this.getChildNode(itemNode,
							new int[] { 0, 0 });
					item.setTitle(hrefNode.getAttribute("title"));
					TagNode imageNode = (TagNode) this.getChildNode(itemNode,
							new int[] { 0, 0, 0 });
					item.setPicUrl(imageNode.getAttribute("src"));
					CompositeTag priceNode = (CompositeTag) this.getChildNode(
							itemNode, new int[] { 1, 0, 0 });
					item.setPrice(Float.parseFloat(priceNode.getStringText()
							.trim()));
					TagNode quantityNode = (TagNode) this.getChildNode(
							itemNode, new int[] { 2, 0 });
					item.setQuantity(Integer.parseInt(quantityNode
							.getAttribute("value")));
					itemList.add(item);
				}
			}
			if (tco.getAddrList().size() > 0) {
				tco.setSelectedAddrId(tco.getAddrList().get(0).getAddrId());
			}
			return tco;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

}
