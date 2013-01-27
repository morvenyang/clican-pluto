package com.clican.appletv.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.response.ItemcatsGetResponse;

public class TaobaoClientImpl extends BaseClient implements TaobaoClient {

	private List<TaobaoCategory> taobaoCategoryList = new ArrayList<TaobaoCategory>();

	private Map<Long, TaobaoCategory> taobaoCategoryMap = new HashMap<Long, TaobaoCategory>();

	private com.taobao.api.TaobaoClient taobaoRestClient;

	public void setTaobaoRestClient(com.taobao.api.TaobaoClient taobaoRestClient) {
		this.taobaoRestClient = taobaoRestClient;
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
				taobaoCategoryList = result;
			} else {
				ItemcatsGetRequest req = new ItemcatsGetRequest();
				req.setFields("cid,parent_cid,name,is_parent");
				req.setParentCid(0L);
				ItemcatsGetResponse response = taobaoRestClient.execute(req);
				List<ItemCat> cats = response.getItemCats();
				Map<String, Long> catMap = new HashMap<String, Long>();
				for (ItemCat ic : cats) {
					catMap.put(ic.getName(), ic.getCid());
					log.debug("0###name:" + ic.getName() + ",cid:"
							+ ic.getCid());
				}
				String url = springProperty.getTaobaoTopCategoryUrl();
				String jsonContent = this.httpGet(url);
				JSONArray jsonArray = JSONArray.fromObject(jsonContent);
				for (int i = 2; i < jsonArray.size(); i++) {
					JSONObject category = jsonArray.getJSONObject(i);
					TaobaoCategory taobaoCategory = convertToTaobaoCategory(
							category, catMap);
					taobaoCategory.setId((long) -i);
					taobaoCategoryList.add(taobaoCategory);
				}
				String content = JSONArray.fromObject(taobaoCategoryList)
						.toString();
				FileUtils.write(taobaoCategoryJsonFile, content, "utf-8");
			}
			for (TaobaoCategory tc1 : taobaoCategoryList) {
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

	private TaobaoCategory convertToTaobaoCategory(JSONObject category,
			Map<String, Long> catMap) throws Exception {
		TaobaoCategory tc = new TaobaoCategory();
		List<TaobaoCategory> tcc = new ArrayList<TaobaoCategory>();
		tc.setChildren(tcc);
		tc.setTitle(category.getString("title"));
		if (category.containsKey("subtitle")) {
			tc.setSubTitle("subtitle");
		}
		tc.setPicUrl(category.getString("pic_url_2x"));
		JSONArray children = category.getJSONArray("children");
		for (int i = 0; i < children.size(); i++) {
			JSONObject child = children.getJSONObject(i);
			TaobaoCategory t = new TaobaoCategory();
			List<TaobaoCategory> tcci = new ArrayList<TaobaoCategory>();
			t.setChildren(tcci);
			t.setTitle(child.getString("title"));
			Long wrongId = child.getLong("catid");
			Long correctId = catMap.get(t.getTitle());

			boolean notfound = false;
			if (correctId != null && !correctId.equals(wrongId)) {
				t.setId(correctId);
				if (log.isDebugEnabled()) {
					log.debug("Replace###correctId:" + correctId + ",wrongId:"
							+ wrongId + ",title:" + t.getTitle());
				}
			} else if (correctId != null && correctId.equals(wrongId)) {
				t.setId(correctId);
				if (log.isDebugEnabled()) {
					log.debug("NoReplace###correctId:" + correctId
							+ ",wrongId:" + wrongId + ",title:" + t.getTitle());
				}
			} else {
				t.setId(wrongId);
				notfound = true;
			}

			t.setHasChild(!child.getBoolean("no_child"));
			tcc.add(t);
			if (t.isHasChild()) {
				ItemcatsGetRequest req = new ItemcatsGetRequest();
				req.setFields("cid,parent_cid,name,is_parent");
				req.setParentCid(t.getId());
				ItemcatsGetResponse response = taobaoRestClient.execute(req);
				List<ItemCat> itemCats = response.getItemCats();
				if (itemCats != null) {
					for (ItemCat ic : itemCats) {
						TaobaoCategory tci = new TaobaoCategory();
						tci.setTitle(ic.getName());
						tci.setId(ic.getCid());
						tcci.add(tci);
					}
				} else {
					t.setHasChild(false);
				}
			}

			if (notfound) {
				if (log.isDebugEnabled()) {
					if (t.getChildren() == null || t.getChildren().size() == 0) {
						log.debug("NotFoundWithoutChildren###correctId:"
								+ correctId + ",wrongId:" + wrongId + ",title:"
								+ t.getTitle());
					} else {
						log.debug("NotFoundButHasChildren###correctId:"
								+ correctId + ",wrongId:" + wrongId + ",title:"
								+ t.getTitle());
					}

				}
			}
		}
		return tc;
	}

	@Override
	public List<TaobaoCategory> getTopCategories() {
		return this.taobaoCategoryList;
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

}
