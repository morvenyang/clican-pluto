package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.QueryRouteResult;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: alibaba.logistics.route.query response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class AlibabaLogisticsRouteQueryResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8143868644763758133L;

	/** 
	 * 查询线路的结果
	 */
	@ApiField("query_route_result")
	private QueryRouteResult queryRouteResult;

	public void setQueryRouteResult(QueryRouteResult queryRouteResult) {
		this.queryRouteResult = queryRouteResult;
	}
	public QueryRouteResult getQueryRouteResult( ) {
		return this.queryRouteResult;
	}

}
