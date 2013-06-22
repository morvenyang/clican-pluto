package com.clican.appletv.core.service.taobao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;

import com.clican.appletv.core.service.BaseClient;
import com.clican.appletv.core.service.taobao.model.TaobaoAccessToken;
import com.clican.appletv.core.service.taobao.model.TaobaoAddress;
import com.clican.appletv.core.service.taobao.model.TaobaoCategory;
import com.clican.appletv.core.service.taobao.model.TaobaoConfirmOrder;
import com.clican.appletv.core.service.taobao.model.TaobaoFare;
import com.clican.appletv.core.service.taobao.model.TaobaoFareRequest;
import com.clican.appletv.core.service.taobao.model.TaobaoFareRequestOrderItem;
import com.clican.appletv.core.service.taobao.model.TaobaoFareRequestOrderItems;
import com.clican.appletv.core.service.taobao.model.TaobaoLove;
import com.clican.appletv.core.service.taobao.model.TaobaoLoveTag;
import com.clican.appletv.core.service.taobao.model.TaobaoOrderByItem;
import com.clican.appletv.core.service.taobao.model.TaobaoOrderByShop;
import com.clican.appletv.core.service.taobao.model.TaobaoPromotion;
import com.clican.appletv.core.service.taobao.model.TaobaoSkuPromotion;
import com.clican.appletv.core.service.taobao.model.TaobaoSkuPromotionWrap;
import com.clican.appletv.ext.htmlparser.EmTag;
import com.clican.appletv.ext.htmlparser.STag;
import com.clican.appletv.ext.htmlparser.StrongTag;
import com.clican.appletv.ext.htmlparser.TbodyTag;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.response.ItemcatsGetResponse;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import gui.ava.html.image.generator.HtmlImageGenerator;

public class TaobaoClientImpl extends BaseClient implements TaobaoClient {

	private Map<Long, TaobaoCategory> taobaoCategoryMap = new HashMap<Long, TaobaoCategory>();

	private com.taobao.api.TaobaoClient taobaoRestClient;

	private List<TaobaoCategory> taobaoTopCategoryList;

	private Map<String, byte[]> confirmOrderImageCache = new ConcurrentHashMap<String, byte[]>();

	private Configuration cfg = new Configuration();

	public void setTaobaoRestClient(com.taobao.api.TaobaoClient taobaoRestClient) {
		this.taobaoRestClient = taobaoRestClient;
	}

	public void setTaobaoTopCategoryList(
			List<TaobaoCategory> taobaoTopCategoryList) {
		this.taobaoTopCategoryList = taobaoTopCategoryList;
	}

	@Override
	public void cacheConfirOrderImage(String deviceId, byte[] image) {
		confirmOrderImageCache.put(deviceId, image);
	}

	@Override
	public byte[] getConfirOrderImage(String deviceId) {
		return confirmOrderImageCache.get(deviceId);
	}

	@Override
	public TaobaoSkuPromotionWrap getPromotion(Item item, String promptionUrl) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Referer",
				"http://item.taobao.com/item.htm?id=" + item.getNumIid());
		String htmlContent = this.httpGet(promptionUrl, headers, null);
		String jsonContent = htmlContent.replaceAll("TB.PromoData =", "")
				.trim();
		JSONObject jsonObj = JSONObject.fromObject(jsonContent);
		JSONArray defList = jsonObj.getJSONArray("def");
		TaobaoSkuPromotionWrap result = new TaobaoSkuPromotionWrap();
		TaobaoSkuPromotion defProm = null;
		for (int i = 0; i < defList.size(); i++) {
			JSONObject obj = defList.getJSONObject(i);
			String title = obj.getString("type");
			String price = null;
			if (obj.containsKey("price")) {
				price = obj.getString("price");
				defProm = new TaobaoSkuPromotion();
				defProm.setTitle(title);
				defProm.setPrice(price);
				break;
			}
		}
		if (defProm == null) {
			return null;
		} else {
			result.setDefaultPromotion(defProm);
			result.setJsonObject(jsonObj);
			return result;
		}
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
						if (ic == null) {
							continue;
						}
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
			cfg.setDirectoryForTemplateLoading(new File(Thread.currentThread()
					.getContextClassLoader().getResource("taobao").getFile()));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
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
		Date startDate = new Date();
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
		Parser promotionParser = Parser.createParser(htmlContent, "utf-8");
		Parser orderParser = Parser.createParser(htmlContent, "utf-8");
		Parser addrParser = Parser.createParser(htmlContent, "utf-8");
		Parser shopParser = Parser.createParser(htmlContent, "utf-8");
		Parser formParser = Parser.createParser(htmlContent, "utf-8");
		ByteArrayOutputStream os = null;
		MemoryCacheImageOutputStream mos = null;
		ByteArrayOutputStream ios = null;
		try {
			addrParser.setNodeFactory(factory);
			shopParser.setNodeFactory(factory);
			formParser.setNodeFactory(factory);
			promotionParser.setNodeFactory(factory);
			orderParser.setNodeFactory(factory);
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
						addr.setAddrParams(addrValueNode
								.getAttribute("ah:params"));
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

			AndFilter formFilter = new AndFilter(new TagNameFilter("form"),
					new HasAttributeFilter("id", "J_Form"));

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
					TagNode fareSelectNode = (TagNode) fareNodeList
							.elementAt(0);
					shop.setFareName(fareSelectNode.getAttribute("name"));
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
								shop.setSelectedFare(fare);
							}
						}
					}
				}

				NodeList itemNodeList = new NodeList();
				orderByShopNode.collectInto(itemNodeList, itemFilter);
				for (int j = 0; j < itemNodeList.size(); j++) {
					TagNode itemNode = (TagNode) itemNodeList.elementAt(j);
					TaobaoOrderByItem item = new TaobaoOrderByItem();
					item.setDataId(itemNode.getAttribute("data-lineid"));
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

			Map<String, String> formMap = new HashMap<String, String>();

			NodeList formNodeList = formParser.parse(formFilter);
			if (formNodeList.size() > 0) {
				FormTag form = (FormTag) formNodeList.elementAt(0);
				NodeList inputs = form.getFormInputs();
				for (int j = 0; j < inputs.size(); j++) {
					InputTag input = (InputTag) inputs.elementAt(j);
					if (input.getAttribute("type").equals("checkbox")) {
						continue;
					}
					String name = input.getAttribute("name");
					if (StringUtils.isNotEmpty(name)) {
						formMap.put(name, input.getAttribute("value"));
					}

				}
				NodeList textareas = form.getFormTextareas();
				for (int j = 0; j < textareas.size(); j++) {
					TextareaTag textarea = (TextareaTag) textareas.elementAt(j);
					String name = textarea.getAttribute("name");
					if (StringUtils.isNotEmpty(name)) {
						formMap.put(name, "");
					}
				}
			}

			TaobaoFareRequest fareRequest = new TaobaoFareRequest();
			fareRequest.setShop_id(formMap.get("shop_id"));
			fareRequest.setBuyer_from(formMap.get("buyer_from"));
			fareRequest.setChannel(formMap.get("channel"));
			fareRequest.setUse_cod(formMap.get("use_cod"));
			fareRequest.setUseSelfCarry(formMap.get("useSelfCarry"));
			for (TaobaoOrderByShop orderShop : shopList) {
				TaobaoFareRequestOrderItems items = new TaobaoFareRequestOrderItems();
				items.setOrderPostFree(false);
				items.setOutOrderId(orderShop.getOutOrderId());
				items.setPostMode(orderShop.getPostMode());
				fareRequest.getOrderItems().add(items);
				for (TaobaoOrderByItem orderItem : orderShop.getItemList()) {
					TaobaoFareRequestOrderItem item = new TaobaoFareRequestOrderItem();
					item.setItem(orderItem.getDataId());
					item.setItemPostFree(false);
					item.setQuantity(orderItem.getQuantity().toString());
					items.getItems().add(item);
				}
			}
			for (TaobaoAddress addr : addrList) {
				fareRequest.setSecondDivisionId(addr.getAreaCode());
				addr.setFareRequest(JSONObject.fromObject(fareRequest)
						.toString());
			}

			AndFilter promotionFilter = new AndFilter(new TagNameFilter(
					"textarea"), new HasAttributeFilter("id",
					"J_PromotionInitData"));
			AndFilter orderFilter = new AndFilter(
					new TagNameFilter("textarea"), new HasAttributeFilter("id",
							"J_OrderInitData"));
			NodeList promotionListNode = promotionParser.parse(promotionFilter);
			if (promotionListNode.size() > 0) {
				TextareaTag promotionNode = (TextareaTag) promotionListNode
						.elementAt(0);
				String promotion = promotionNode.getValue();
				JSONObject promotionJson = JSONObject.fromObject(promotion);
				JSONObject relation = promotionJson.getJSONObject("relation");
				JSONArray crossId = relation.getJSONArray("cross_id");
				JSONObject proms = promotionJson.getJSONObject("promos");
				Map<String, List<String>> shopMap = new HashMap<String, List<String>>();
				for (int i = 0; i < crossId.size(); i++) {
					String shopId = crossId.getString(i);
					shopMap.put(shopId, new ArrayList<String>());
					JSONArray itemArray = relation.getJSONArray(shopId);
					for (int j = 0; j < itemArray.size(); j++) {
						String itemId = itemArray.getString(j);
						shopMap.get(shopId).add(itemId);
					}
				}

				JSONObject orders = promotionJson.getJSONObject("orders");
				for (TaobaoOrderByShop shop : shopList) {
					String shopId = "b_" + shop.getOutOrderId();
					JSONObject shopPromotionJson = orders.getJSONObject(shopId);
					if (shopPromotionJson.containsKey("bundle")) {
						String value = shopPromotionJson.getString("bundle");
						JSONObject valueJson = shopPromotionJson.getJSONObject(
								"bundles").getJSONObject(value);
						JSONArray promArray = proms.getJSONObject(value)
								.getJSONArray("unused");
						TaobaoPromotion shopPromotion = new TaobaoPromotion();
						shopPromotion.setTitle(valueJson.getString("title"));
						shopPromotion.setName("bundleList_" + shopId);
						shopPromotion.setDiscount(valueJson
								.getDouble("discount") / 100);
						if (promArray.size() > 0) {
							shopPromotion.setValue(promArray.getString(0));
						}
						if (StringUtils.isNotEmpty(shopPromotion.getValue())) {
							formMap.put(shopPromotion.getName(),
									shopPromotion.getValue());
						}
						shop.setPromotion(shopPromotion);
					}
					for (TaobaoOrderByItem item : shop.getItemList()) {
						String dataId = item.getDataId();
						JSONObject itemPromotionJson = orders
								.getJSONObject(dataId);
						if (itemPromotionJson.containsKey("bundle")) {
							String value = itemPromotionJson
									.getString("bundle");
							JSONObject valueJson = itemPromotionJson
									.getJSONObject("bundles").getJSONObject(
											value);
							JSONArray promArray = proms.getJSONObject(value)
									.getJSONArray("unused");
							TaobaoPromotion itemPromotion = new TaobaoPromotion();
							itemPromotion
									.setTitle(valueJson.getString("title"));
							itemPromotion.setName("bundleList_" + dataId);
							itemPromotion.setDiscount(valueJson
									.getDouble("discount") / 100);
							if (promArray.size() > 0) {
								itemPromotion.setValue(promArray.getString(0));
							}
							if (StringUtils
									.isNotEmpty(itemPromotion.getValue())) {
								formMap.put(itemPromotion.getName(),
										itemPromotion.getValue());
							}
							item.setPromotion(itemPromotion);
							formMap.put(itemPromotion.getName(),
									itemPromotion.getValue());
						}
					}
				}
			}
			NodeList orderListNode = orderParser.parse(orderFilter);
			double tcoTotal = 0;
			if (orderListNode.size() > 0) {
				TextareaTag orderNode = (TextareaTag) orderListNode
						.elementAt(0);
				String order = orderNode.getValue();
				JSONObject orderJson = JSONObject.fromObject(order);
				JSONObject ordersJson = orderJson.getJSONObject("orders");
				for (TaobaoOrderByShop shop : shopList) {
					String shopId = "b_" + shop.getOutOrderId();
					JSONArray shopFareJsons = ordersJson.getJSONObject(shopId)
							.getJSONArray("postages");
					for (int i = 0; i < shopFareJsons.size(); i++) {
						JSONObject shopFareJson = shopFareJsons
								.getJSONObject(i);
						if (shopFareJson.getBoolean("select")) {
							shop.getSelectedFare().setFareFee(
									shopFareJson.getDouble("fare") / 100);
						}
					}
					double total = 0;
					for (TaobaoOrderByItem item : shop.getItemList()) {
						total += item.getActualPrice();
					}
					total += shop.getSelectedFare().getFareFee();
					if (shop.getPromotion() != null) {
						total = total - shop.getPromotion().getDiscount();
					}
					shop.setTotal(total);
					tcoTotal += total;
				}

			}
			tco.setTotal(tcoTotal);
			tco.setForms(formMap);
			Map<String, Object> rootMap = new HashMap<String, Object>();
			rootMap.put("tco", tco);
			Template template = cfg.getTemplate("confirm.ftl", "utf-8");
			os = new ByteArrayOutputStream();
			Writer out = new OutputStreamWriter(os, "utf-8");
			Environment env = template
					.createProcessingEnvironment(rootMap, out);
			env.setOutputEncoding("utf-8");
			env.process();
			out.flush();
			String content = new String(os.toByteArray(), "utf-8");
			HtmlImageGenerator generator = new HtmlImageGenerator();
			generator.loadHtml(content);
			ios = new ByteArrayOutputStream();
			mos = new MemoryCacheImageOutputStream(ios);
			ImageIO.write(generator.getBufferedImage(), "png", mos);
			tco.setConfirmOrderImage(ios.toByteArray());
			Date endDate = new Date();
			if (log.isDebugEnabled()) {
				log.debug("spend " + (endDate.getTime() - startDate.getTime())
						+ " ms to generate confirm order");
			}
			return tco;
		} catch (Exception e) {
			log.error("", e);
			return null;
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
			try {
				ios.close();
			} catch (Exception e) {
				log.error("", e);
			}
			try {
				mos.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}
}
