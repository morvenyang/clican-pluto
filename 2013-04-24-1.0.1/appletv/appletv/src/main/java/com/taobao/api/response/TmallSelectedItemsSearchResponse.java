package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.SelectedItem;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: tmall.selected.items.search response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TmallSelectedItemsSearchResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7334188593833177125L;

	/** 
	 * 天猫精选商品列表。同一天之内，同一个appkey请求同一个cid得到的商品列表是固定的，所以每天只需请求一次即可
	 */
	@ApiListField("item_list")
	@ApiField("selected_item")
	private List<SelectedItem> itemList;

	public void setItemList(List<SelectedItem> itemList) {
		this.itemList = itemList;
	}
	public List<SelectedItem> getItemList( ) {
		return this.itemList;
	}

}
