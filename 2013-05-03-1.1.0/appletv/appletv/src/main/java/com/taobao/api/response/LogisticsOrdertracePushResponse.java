package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Shipping;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.logistics.ordertrace.push response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class LogisticsOrdertracePushResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8127559638828351948L;

	/** 
	 * 返回结果是否成功is_success
	 */
	@ApiField("shipping")
	private Shipping shipping;

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
	public Shipping getShipping( ) {
		return this.shipping;
	}

}
