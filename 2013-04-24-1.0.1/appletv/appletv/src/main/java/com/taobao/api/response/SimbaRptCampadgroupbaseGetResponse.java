package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.campadgroupbase.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptCampadgroupbaseGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7393984811416453882L;

	/** 
	 * 推广计划下推广组的基础数据列表
	 */
	@ApiField("rpt_campadgroup_base_list")
	private String rptCampadgroupBaseList;

	public void setRptCampadgroupBaseList(String rptCampadgroupBaseList) {
		this.rptCampadgroupBaseList = rptCampadgroupBaseList;
	}
	public String getRptCampadgroupBaseList( ) {
		return this.rptCampadgroupBaseList;
	}

}
