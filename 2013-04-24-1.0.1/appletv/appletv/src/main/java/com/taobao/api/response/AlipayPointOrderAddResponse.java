package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: alipay.point.order.add response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class AlipayPointOrderAddResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5535484151619812459L;

	/** 
	 * 支付宝集分宝发放流水号
	 */
	@ApiField("alipay_order_no")
	private String alipayOrderNo;

	/** 
	 * 充值结果：SUCCESS表示成功，其他表示失败
	 */
	@ApiField("result_code")
	private Boolean resultCode;

	public void setAlipayOrderNo(String alipayOrderNo) {
		this.alipayOrderNo = alipayOrderNo;
	}
	public String getAlipayOrderNo( ) {
		return this.alipayOrderNo;
	}

	public void setResultCode(Boolean resultCode) {
		this.resultCode = resultCode;
	}
	public Boolean getResultCode( ) {
		return this.resultCode;
	}

}
