package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.SearchOrderResult;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.trip.jipiao.agent.order.search response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TripJipiaoAgentOrderSearchResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8883245693338611426L;

	/** 
	 * 国内机票订单搜索返回结果对象
	 */
	@ApiField("search_result")
	private SearchOrderResult searchResult;

	public void setSearchResult(SearchOrderResult searchResult) {
		this.searchResult = searchResult;
	}
	public SearchOrderResult getSearchResult( ) {
		return this.searchResult;
	}

}
