package com.clican.appletv.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.taobao.TaobaoClientImpl;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Sku;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.ShopGetRequest;
import com.taobao.api.request.TaobaokeItemsRelateGetRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.ShopGetResponse;
import com.taobao.api.response.TaobaokeItemsRelateGetResponse;

public class TaobaoTestCase extends BaseServiceTestCase {

	private SpringProperty springProperty;
	private TaobaoClient taobaoRestClient;
	private com.clican.appletv.core.service.taobao.TaobaoClient taobaoClient;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setTaobaoRestClient(TaobaoClient taobaoRestClient) {
		this.taobaoRestClient = taobaoRestClient;
	}

	public void setTaobaoClient(
			com.clican.appletv.core.service.taobao.TaobaoClient taobaoClient) {
		this.taobaoClient = taobaoClient;
	}

	public void testGetCategorys() throws Exception {
		// List<TaobaoCategory> list = taobaoClient.getTopCategories();
		// StringBuffer sb = new StringBuffer();
		// TaobaoCategory.toString(list, sb, "\n");
		// log.debug(sb.toString());
	}

	public void testGetCategorysByParentId() throws Exception {
		// ItemcatsGetRequest req = new ItemcatsGetRequest();
		// req.setFields("cid,parent_cid,name,is_parent");
		// req.setParentCid(50065355L);
		// ItemcatsGetResponse response = taobaoRestClient.execute(req);
		// List<ItemCat> itemCats = response.getItemCats();
		// log.debug(itemCats.size());
	}

	private String[] getTokenAndTid() {
		TaobaoClientImpl client = (TaobaoClientImpl) taobaoClient;
		Map<String, String> nameValueMap = new HashMap<String, String>();
		nameValueMap.put("TPL_username", "clicanclican");
		nameValueMap.put("TPL_password", "clican@810428");
		PostResponse pr = client.httpPost(
				"https://login.taobao.com/member/login.jhtml", null,
				nameValueMap, "application/x-www-form-urlencoded", "utf-8",
				null, null);
		Map<String, String> header2 = new HashMap<String, String>();
		header2.put("Cookie", pr.getCookieString());
		PostResponse pr2 = client.httpGetForCookie(
				"http://favorite.taobao.com/collect_list.htm", header2, null);
		String[] result = new String[3];
		String token = pr2.getCookieMap().get("_tb_token_");
		result[0] = token;
		String content = pr2.getContent();
		int start = content.indexOf("tid=")+4;
		int end = content.indexOf("&", start);
		result[1] = content.substring(start,end);
		result[2] = pr.getCookieString();
		return result;
	}

	public void testLoginAndAddFavorite() throws Exception {
		// TaobaoClientImpl client = (TaobaoClientImpl) taobaoClient;
		// Map<String, String> nameValueMap = new HashMap<String, String>();
		// nameValueMap.put("TPL_username", "");
		// nameValueMap.put("TPL_password", "");
		// PostResponse pr = client.httpPost(
		// "https://login.taobao.com/member/login.jhtml", null,
		// nameValueMap, "application/x-www-form-urlencoded", "utf-8",
		// null, null);
		// Map<String, String> header2 = new HashMap<String, String>();
		// header2.put("Cookie", pr.getCookieString());
		// // String content2 =
		// //
		// client.httpGet("http://favorite.taobao.com/collect_list.htm",header2,null);
		// //
		// http://favorite.taobao.com/json/collect_list_chunk.htm?itemtype=1&isBigImgShow=true&orderby=time&startrow=0&chunkSize=12&chunkNum=1&deleNum=0
		// PostResponse pr2 = client.httpGetForCookie(
		// "http://favorite.taobao.com/collect_list.htm", header2, null);
		// log.debug(pr2.getContent());
		// String token = pr2.getCookieMap().get("_tb_token_");
		// log.debug("token=" + token);
		// assertNotNull(token);
		// taobaoClient.addFavorite(1, 1, null, null, 12729301574L,
		// "_tb_token_="
		// + token + ";" + pr.getCookieString(), token);
	}

	public void testGetItemsFromStore() throws Exception {

		ShopGetRequest req1 = new ShopGetRequest();
		req1.setFields("sid,cid,title,nick,desc,bulletin,pic_path,created,modified");
		req1.setNick("海尚丽人旗舰店");
		ShopGetResponse response1 = taobaoRestClient.execute(req1);
		Long sid = response1.getShop().getSid();
		String shopUrl = "http://shop" + sid + ".taobao.com";
		String content = ((TaobaoClientImpl) taobaoClient).httpGet(shopUrl);
		int start = content.indexOf("userid=") + "userid=".length();
		int end = content.indexOf(";", start);
		Long sellerId = Long.parseLong(content.substring(start, end).trim());

		TaobaokeItemsRelateGetRequest req2 = new TaobaokeItemsRelateGetRequest();
		req2.setRelateType(4L);
		req2.setSellerId(sellerId);
		req2.setFields("num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume");
		TaobaokeItemsRelateGetResponse response2 = taobaoRestClient
				.execute(req2);
	}

	public void testGetItemsBySellerCategory() throws Exception {
		List<Long> itemIds = taobaoClient.getItemsBySellerCategory(68710510L,
				661186880L, "tL%2FD3rbM0ORU0PQ%3D");
		Set<Long> itemIdSet = new HashSet<Long>();
		itemIdSet.addAll(itemIds);
		log.debug(itemIdSet.size());
	}

	public void testAddToShoppingCart() throws Exception {
		String[] tokenAndTid = this.getTokenAndTid();
		log.debug("toke="+tokenAndTid[0]+",tid="+tokenAndTid[1]);
		Long itemId = 20461976769L;
		//itemId=15211031341L;
		ItemGetRequest req=new ItemGetRequest();
		req.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
		req.setNumIid(itemId);
		ItemGetResponse response = taobaoRestClient.execute(req );
		List<Sku> skuList = response.getItem().getSkus();
		Long skuId= skuList.get(0).getSkuId();
		for(Sku sku:skuList){
			if(sku.getQuantity()>0){
				skuId = sku.getSkuId();
				break;
			}
		}
		String url = "http://cart.taobao.com/add_cart_item.htm?item_id="+itemId+"&bankfrom=&outer_id="+skuId+"&outer_id_type=2&quantity=1&nekot="+Calendar.getInstance().getTimeInMillis()+"&ct="+tokenAndTid[1];
		Map<String, String> header = new HashMap<String, String>();
		header.put("Cookie", tokenAndTid[2]);
		String html = ((TaobaoClientImpl)taobaoClient).httpGet(url, header, null);
		log.debug(html);
	}
}
