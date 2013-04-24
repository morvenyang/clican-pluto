package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: alipay.ebpp.bill.payurl.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class AlipayEbppBillPayurlGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7268682951921458436L;

	/** 
	 * 付款页面地址
	 */
	@ApiField("pay_url")
	private String payUrl;

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getPayUrl( ) {
		return this.payUrl;
	}

}
