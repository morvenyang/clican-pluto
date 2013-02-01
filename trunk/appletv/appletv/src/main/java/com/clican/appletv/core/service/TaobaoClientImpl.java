package com.clican.appletv.core.service;

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
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;
import com.clican.appletv.core.model.TaobaoLove;
import com.clican.appletv.core.model.TaobaoLoveTag;
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
		String c = "_tb_token_=f3d3a11f7e5eb;  t=e61a4f0b9d46d01f9bf1714203a1b1f1; unb=82478075; _nk_=clicanclican; _l_g_=Ug%3D%3D; cookie2=86db46da144cc35e0410bd51e28862f9; tracknick=clicanclican; sg=n5f; lastgetwwmsg=MTM1OTM2NDI0Mg%3D%3D; cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D; l=clicanclican::1359367854907::11; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; uc1=lltime=1359363765&cookie14=UoLZVdan01UWGA%3D%3D&existShop=false&cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&cookie21=W5iHLLyFe3xm&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364384131%26l%3D1359364251900";
		c = "_tb_token_=f3d3a11f7e5eb;   _l_g_=Ug%3D%3D; cookie2=86db46da144cc35e0410bd51e28862f9; tracknick=clicanclican; sg=n5f; lastgetwwmsg=MTM1OTM2NDI0Mg%3D%3D; cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D; l=clicanclican::1359367854907::11; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; uc1=lltime=1359363765&cookie14=UoLZVdan01UWGA%3D%3D&existShop=false&cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&cookie21=W5iHLLyFe3xm&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364384131%26l%3D1359364251900";
		c = "_tb_token_=f3d3a11f7e5eb;  cookie2=86db46da144cc35e0410bd51e28862f9; tracknick=clicanclican; sg=n5f; lastgetwwmsg=MTM1OTM2NDI0Mg%3D%3D; cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D; l=clicanclican::1359367854907::11; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; uc1=lltime=1359363765&cookie14=UoLZVdan01UWGA%3D%3D&existShop=false&cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&cookie21=W5iHLLyFe3xm&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364384131%26l%3D1359364251900";
		c = "_tb_token_=f3d3a11f7e5eb;  cookie2=86db46da144cc35e0410bd51e28862f9;  sg=n5f; lastgetwwmsg=MTM1OTM2NDI0Mg%3D%3D; cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D; l=clicanclican::1359367854907::11; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; uc1=lltime=1359363765&cookie14=UoLZVdan01UWGA%3D%3D&existShop=false&cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&cookie21=W5iHLLyFe3xm&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364384131%26l%3D1359364251900";
		c = "_tb_token_=f3d3a11f7e5eb;  cookie2=86db46da144cc35e0410bd51e28862f9;  sg=n5f; lastgetwwmsg=MTM1OTM2NDI0Mg%3D%3D; cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D; l=clicanclican::1359367854907::11; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; uc1=lltime=1359363765&cookie14=UoLZVdan01UWGA%3D%3D&existShop=false&cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&cookie21=W5iHLLyFe3xm&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364384131%26l%3D1359364251900";
		c = "_tb_token_=f3d3a11f7e5eb; cookie2=86db46da144cc35e0410bd51e28862f9; sg=n5f; lastgetwwmsg=MTM1OTM2NDY5Ng%3D%3D; cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D; l=clicanclican::1359368308304::11; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; uc1=lltime=1359364242&cookie14=UoLZVdan01CPCA%3D%3D&existShop=false&cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&cookie21=V32FPkk%2FgPzW&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364744220%26l%3D1359364700772";
		c = "_tb_token_=f3d3a11f7e5eb; cookie2=86db46da144cc35e0410bd51e28862f9; sg=n5f;  cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D; l=clicanclican::1359368308304::11; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; uc1=lltime=1359364242&cookie14=UoLZVdan01CPCA%3D%3D&existShop=false&cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&cookie21=V32FPkk%2FgPzW&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364744220%26l%3D1359364700772";
		c = "_tb_token_=f3d3a11f7e5eb; cookie2=86db46da144cc35e0410bd51e28862f9; sg=n5f;  cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D;  x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; uc1=lltime=1359364242&cookie14=UoLZVdan01CPCA%3D%3D&existShop=false&cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&cookie21=V32FPkk%2FgPzW&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364744220%26l%3D1359364700772";
		c = "_tb_token_=f3d3a11f7e5eb; cookie2=86db46da144cc35e0410bd51e28862f9; sg=n5f;  cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D;  uc1=lltime=1359364242&cookie14=UoLZVdan01CPCA%3D%3D&existShop=false&cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&cookie21=V32FPkk%2FgPzW&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; mpp=t%3D1%26m%3D%26h%3D1359364744220%26l%3D1359364700772";
		c = "_tb_token_=f3d3a11f7e5eb; cookie2=86db46da144cc35e0410bd51e28862f9; sg=n5f;  cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D;  uc1=lltime=1359364242&cookie14=UoLZVdan01CPCA%3D%3D&existShop=false&cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&cookie21=V32FPkk%2FgPzW&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; ";
		c = "_tb_token_=f3d3a11f7e5eb; cookie2=86db46da144cc35e0410bd51e28862f9; sg=n5f;  cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D;  uc1=lltime=1359364242&cookie14=UoLZVdan01CPCA%3D%3D&existShop=false&cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&cookie21=V32FPkk%2FgPzW&tag=1&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D;";
		c = "_tb_token_=e71bee60b53ae; cookie2=6a5e831e3602512995fcad906ea7c47d; sg=n5f;  cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D;  uc1=lltime=1359364696&cookie14=UoLZVdan0rkVxQ%3D%3D&existShop=false&cookie16=Vq8l%2BKCLySLZMFWHxqs8fwqnEw%3D%3D&cookie21=W5iHLLyFe3xm&tag=1&cookie15=URm48syIIVrSKA%3D%3D;";
		header.put("Cookie", cookie);
		String url2 = "http://favorite.taobao.com/popup/add_collection.htm";
		PostResponse pr2 = this.httpPost(url2, null, nameValueMap,
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

}
