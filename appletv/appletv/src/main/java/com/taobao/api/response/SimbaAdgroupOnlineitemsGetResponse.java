package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.SimbaItemPartition;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroup.onlineitems.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupOnlineitemsGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4632662642331668581L;

	/** 
	 * 带分页的淘宝商品
	 */
	@ApiField("page_item")
	private SimbaItemPartition pageItem;

	public void setPageItem(SimbaItemPartition pageItem) {
		this.pageItem = pageItem;
	}
	public SimbaItemPartition getPageItem( ) {
		return this.pageItem;
	}

}
