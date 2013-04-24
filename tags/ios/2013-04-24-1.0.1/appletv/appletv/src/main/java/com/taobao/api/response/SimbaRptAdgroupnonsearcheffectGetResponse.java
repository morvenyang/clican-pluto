package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.adgroupnonsearcheffect.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptAdgroupnonsearcheffectGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2329573217558824388L;

	/** 
	 * 非搜索推广组效果对象
	 */
	@ApiField("rpt_adgroup_nonsearch_effect")
	private String rptAdgroupNonsearchEffect;

	public void setRptAdgroupNonsearchEffect(String rptAdgroupNonsearchEffect) {
		this.rptAdgroupNonsearchEffect = rptAdgroupNonsearchEffect;
	}
	public String getRptAdgroupNonsearchEffect( ) {
		return this.rptAdgroupNonsearchEffect;
	}

}
