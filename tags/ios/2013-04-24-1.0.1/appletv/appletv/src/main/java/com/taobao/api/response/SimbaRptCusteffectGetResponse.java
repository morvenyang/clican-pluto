package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.custeffect.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptCusteffectGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5328459325151129933L;

	/** 
	 * 账户效果数据返回结果
	 */
	@ApiField("rpt_cust_effect_list")
	private String rptCustEffectList;

	public void setRptCustEffectList(String rptCustEffectList) {
		this.rptCustEffectList = rptCustEffectList;
	}
	public String getRptCustEffectList( ) {
		return this.rptCustEffectList;
	}

}
