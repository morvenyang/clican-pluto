package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.adgroupeffect.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptAdgroupeffectGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2428564387468514769L;

	/** 
	 * 推广组效果报表数据对象
	 */
	@ApiField("rpt_adgroup_effect_list")
	private String rptAdgroupEffectList;

	public void setRptAdgroupEffectList(String rptAdgroupEffectList) {
		this.rptAdgroupEffectList = rptAdgroupEffectList;
	}
	public String getRptAdgroupEffectList( ) {
		return this.rptAdgroupEffectList;
	}

}
