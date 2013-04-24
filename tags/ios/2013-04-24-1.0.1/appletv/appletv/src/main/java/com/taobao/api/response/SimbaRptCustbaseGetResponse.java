package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.custbase.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptCustbaseGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2449987777973772827L;

	/** 
	 * 用户帐户结果
	 */
	@ApiField("rpt_cust_base_list")
	private String rptCustBaseList;

	public void setRptCustBaseList(String rptCustBaseList) {
		this.rptCustBaseList = rptCustBaseList;
	}
	public String getRptCustBaseList( ) {
		return this.rptCustBaseList;
	}

}
