package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.adgroupkeywordeffect.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptAdgroupkeywordeffectGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2374148478589357993L;

	/** 
	 * 词效果数据返回结果
	 */
	@ApiField("rpt_adgroupkeyword_effect_list")
	private String rptAdgroupkeywordEffectList;

	public void setRptAdgroupkeywordEffectList(String rptAdgroupkeywordEffectList) {
		this.rptAdgroupkeywordEffectList = rptAdgroupkeywordEffectList;
	}
	public String getRptAdgroupkeywordEffectList( ) {
		return this.rptAdgroupkeywordEffectList;
	}

}
