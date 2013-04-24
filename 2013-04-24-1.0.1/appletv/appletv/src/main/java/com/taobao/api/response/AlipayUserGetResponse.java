package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.AlipayUserDetail;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: alipay.user.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class AlipayUserGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3823459368167487572L;

	/** 
	 * 支付宝用户信息
	 */
	@ApiField("alipay_user_detail")
	private AlipayUserDetail alipayUserDetail;

	public void setAlipayUserDetail(AlipayUserDetail alipayUserDetail) {
		this.alipayUserDetail = alipayUserDetail;
	}
	public AlipayUserDetail getAlipayUserDetail( ) {
		return this.alipayUserDetail;
	}

}
