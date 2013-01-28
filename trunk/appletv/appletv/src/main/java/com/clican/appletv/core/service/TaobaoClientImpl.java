package com.clican.appletv.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;
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
	public boolean addFavorite(Integer itemtype, Integer isMall, Integer isLp,
			Integer isTaohua, Long id, String cookie, String token) {
		Map<String, String> nameValueMap = new HashMap<String, String>();
		nameValueMap.put("itemtype", "1");
		nameValueMap.put("isTmall", "1");
		nameValueMap.put("isLp", "");
		nameValueMap.put("isTaohua", "");
		nameValueMap.put("id", id.toString());
		nameValueMap.put("_tb_token_", "e71bee60b53ae");
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
		header.put("Cookie", c);
		// String url =
		// "http://favorite.taobao.com/popup/add_collection.htm?id=12729301574&itemtype=1&scjjc=1&_tb_token_="+token+"&t="+Calendar.getInstance().getTimeInMillis();
		// header.put(
		// "Cookie",
		// "cna=q7h7CaJDmGoCAQ/bmUsaWfGb; __utma=6906807.320180861.1359014561.1359014561.1359014561.1; __utmz=6906807.1359014561.1.1.utmcsr=mtop.taobao.com|utmccn=(referral)|utmcmd=referral|utmcct=/; v=0; mt=ci=0_1; _tb_token_=ede69ee51ee17; swfstore=229989; x=e%3D1%26p%3D*%26s%3D0%26c%3D1%26f%3D0%26g%3D0%26t%3D0; l=clicanclican::1359355944079::11; tg=0; _cc_=Vq8l%2BKCLiw%3D%3D; t=9f05c88e7aa4b54d21d7afdafded91e9; unb=82478075; _nk_=clicanclican; _l_g_=Ug%3D%3D; cookie2=4785494734a5562015ef76a6ac1c3575; tracknick=clicanclican; sg=n5f; lastgetwwmsg=MTM1OTM1MjQzOQ%3D%3D; cookie1=VAFYwFXZxCjbTmyCB%2BKJXAKeZjYGL0WQ%2BK6Ce3BP1YE%3D; cookie17=W8twrd9AJm0%3D; uc1=lltime=1359352046&cookie14=UoLZVdakrkfIIA%3D%3D&existShop=false&cookie16=UtASsssmPlP%2Ff1IHDsDaPRu%2BPw%3D%3D&cookie21=UIHiLt3xTIkz&tag=1&cookie15=UtASsssmOIJ0bQ%3D%3D;");
		// PostResponse pr = this.httpGetForCookie(url, header, null);
		// log.debug(pr.getContent());
		String url2 = "http://favorite.taobao.com/popup/add_collection.htm";
		PostResponse pr2 = this.httpPost(url2, null, nameValueMap,
				"application/x-www-form-urlencoded", "utf-8", header, null);
//		cookie +=pr2.getCookies();
//		header.put("Cookie", cookie);
//		PostResponse pr3 = this
//				.httpGetForCookie(
//						"http://favorite.taobao.com/popup/add_collection_2.htm?id=18180872462&itemtype=1&is_tmall=1&is_lp=&is_taohua=",
//						header, null);
//		log.debug(pr3.getContent());
//		cookie +=pr3.getCookies();
//		header.put("Cookie", cookie);
//		String url4 = "http://favorite.taobao.com/popup/add_collection.htm";
//		PostResponse pr4 = this.httpPost(url4, null, nameValueMap,
//				"application/x-www-form-urlencoded", "utf-8", header, null);
//
//		log.debug(pr4.getContent());
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

}
