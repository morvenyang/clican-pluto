package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.AreaOption;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.campaign.areaoptions.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCampaignAreaoptionsGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5725412471254534579L;

	/** 
	 * 推广计划所有可设置的投放地域
	 */
	@ApiListField("area_options")
	@ApiField("area_option")
	private List<AreaOption> areaOptions;

	public void setAreaOptions(List<AreaOption> areaOptions) {
		this.areaOptions = areaOptions;
	}
	public List<AreaOption> getAreaOptions( ) {
		return this.areaOptions;
	}

}
