package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.campaigneffect.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptCampaigneffectGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3556354218396182298L;

	/** 
	 * 推广计划效果报表数据对象
	 */
	@ApiField("rpt_campaign_effect_list")
	private String rptCampaignEffectList;

	public void setRptCampaignEffectList(String rptCampaignEffectList) {
		this.rptCampaignEffectList = rptCampaignEffectList;
	}
	public String getRptCampaignEffectList( ) {
		return this.rptCampaignEffectList;
	}

}
