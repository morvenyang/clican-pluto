package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.demographiceffect.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptDemographiceffectGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1163913131461869859L;

	/** 
	 * 推广计划下的人群维度效果数据查询
	 */
	@ApiField("rpt_demographic_effect")
	private String rptDemographicEffect;

	public void setRptDemographicEffect(String rptDemographicEffect) {
		this.rptDemographicEffect = rptDemographicEffect;
	}
	public String getRptDemographicEffect( ) {
		return this.rptDemographicEffect;
	}

}
