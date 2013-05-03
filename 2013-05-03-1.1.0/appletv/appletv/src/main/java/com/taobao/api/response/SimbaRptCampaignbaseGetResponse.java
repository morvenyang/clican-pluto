package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.campaignbase.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptCampaignbaseGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 6247188984737481548L;

	/** 
	 * 推广计划查询结果
	 */
	@ApiField("rpt_campaign_base_list")
	private String rptCampaignBaseList;

	public void setRptCampaignBaseList(String rptCampaignBaseList) {
		this.rptCampaignBaseList = rptCampaignBaseList;
	}
	public String getRptCampaignBaseList( ) {
		return this.rptCampaignBaseList;
	}

}
