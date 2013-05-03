package com.taobao.api.response;

import java.util.List;

import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.TaobaokeShop;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

public class TaobaokeShopsConvertResponse extends TaobaoResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1558709371978501280L;

	
	@ApiListField("taobaoke_shops")
	@ApiField("taobaoke_shop")
	private List<TaobaokeShop> taobaokeShops;


	public List<TaobaokeShop> getTaobaokeShops() {
		return taobaokeShops;
	}


	public void setTaobaokeShops(List<TaobaokeShop> taobaokeShops) {
		this.taobaokeShops = taobaokeShops;
	}
	
	
}
