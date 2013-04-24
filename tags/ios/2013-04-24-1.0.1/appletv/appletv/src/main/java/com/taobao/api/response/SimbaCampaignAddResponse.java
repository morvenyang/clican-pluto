package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Campaign;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.campaign.add response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCampaignAddResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5387613846175776621L;

	/** 
	 * 创建的推广计划
	 */
	@ApiField("campaign")
	private Campaign campaign;

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
	public Campaign getCampaign( ) {
		return this.campaign;
	}

}
