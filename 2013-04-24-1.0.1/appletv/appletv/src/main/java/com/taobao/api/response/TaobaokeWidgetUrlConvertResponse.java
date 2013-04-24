package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.TaobaokeItem;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.taobaoke.widget.url.convert response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TaobaokeWidgetUrlConvertResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4534625239961227422L;

	/** 
	 * 只返回click_url
	 */
	@ApiField("taobaoke_item")
	private TaobaokeItem taobaokeItem;

	public void setTaobaokeItem(TaobaokeItem taobaokeItem) {
		this.taobaokeItem = taobaokeItem;
	}
	public TaobaokeItem getTaobaokeItem( ) {
		return this.taobaokeItem;
	}

}
