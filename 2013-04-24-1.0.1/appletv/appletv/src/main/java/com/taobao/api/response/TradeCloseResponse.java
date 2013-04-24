package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Trade;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.trade.close response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TradeCloseResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1637387648413967469L;

	/** 
	 * 关闭交易时返回的Trade信息，可用字段有tid和modified
	 */
	@ApiField("trade")
	private Trade trade;

	public void setTrade(Trade trade) {
		this.trade = trade;
	}
	public Trade getTrade( ) {
		return this.trade;
	}

}
