package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.WidgetItem;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.widget.itempanel.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class WidgetItempanelGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3754958237416447399L;

	/** 
	 * 服务端签名后的添加链接，isv在使用的时候前面加上“http://gw.api.taobao.com/widget?”后面加上用户选择的sku_id和购买的quantity即可生成完整的购买链接。
	 */
	@ApiField("add_url")
	private String addUrl;

	/** 
	 * 取得的商品相关信息，如果不指定fields返回所有字段，如果指定了fields但是都是无效字段，返回的item结构体中字段为空
	 */
	@ApiField("item")
	private WidgetItem item;

	public void setAddUrl(String addUrl) {
		this.addUrl = addUrl;
	}
	public String getAddUrl( ) {
		return this.addUrl;
	}

	public void setItem(WidgetItem item) {
		this.item = item;
	}
	public WidgetItem getItem( ) {
		return this.item;
	}

}
