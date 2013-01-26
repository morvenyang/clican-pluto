package com.clican.appletv.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.response.ItemcatsGetResponse;

public class TaobaoClientImpl extends BaseClient implements TaobaoClient {

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

	public void init() {
		try {
			String url = springProperty.getTaobaoTopCategoryUrl();
			String jsonContent = this.httpGet(url);
			JSONArray jsonArray = JSONArray.fromObject(jsonContent);
			for (int i = 2; i < jsonArray.size(); i++) {
				JSONObject category = jsonArray.getJSONObject(i);
				TaobaoCategory taobaoCategory = new TaobaoCategory();
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

	private TaobaoCategory convertToTaobaoCategory(JSONObject category)
			throws Exception {
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
			t.setTitle(child.getString("title"));
			t.setId(child.getLong("catid"));
			t.setHasChild(!child.getBoolean("no_child"));
			tcc.add(t);
			if (t.isHasChild()) {
				ItemcatsGetRequest req = new ItemcatsGetRequest();
				req.setFields("cid,parent_cid,name,is_parent");
				req.setParentCid(t.getId());
				ItemcatsGetResponse response = taobaoRestClient.execute(req);
				List<ItemCat> itemCats = response.getItemCats();
			}
		}
		return tc;
	}

	@Override
	public List<TaobaoCategory> getTopCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaobaoCategory> getCategories(Long parentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
