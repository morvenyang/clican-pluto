package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Shipping;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.logistics.orderstore.push response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class LogisticsOrderstorePushResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1241336562324245682L;

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
