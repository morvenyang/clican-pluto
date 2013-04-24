package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.account.balance.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAccountBalanceGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3517397521225756832L;

	/** 
	 * 实时余额，”元”为单位
	 */
	@ApiField("balance")
	private String balance;

	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getBalance( ) {
		return this.balance;
	}

}
