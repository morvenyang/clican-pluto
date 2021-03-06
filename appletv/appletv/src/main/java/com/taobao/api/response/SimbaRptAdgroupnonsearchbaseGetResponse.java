package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.rpt.adgroupnonsearchbase.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaRptAdgroupnonsearchbaseGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4628923991599748286L;

	/** 
	 * 非搜索推广组基础对象
	 */
	@ApiField("rpt_adgroup_nonsearch_base")
	private String rptAdgroupNonsearchBase;

	public void setRptAdgroupNonsearchBase(String rptAdgroupNonsearchBase) {
		this.rptAdgroupNonsearchBase = rptAdgroupNonsearchBase;
	}
	public String getRptAdgroupNonsearchBase( ) {
		return this.rptAdgroupNonsearchBase;
	}

}
