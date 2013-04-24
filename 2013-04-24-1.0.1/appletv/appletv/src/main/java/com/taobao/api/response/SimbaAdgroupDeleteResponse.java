package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.ADGroup;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroup.delete response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupDeleteResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1457986517423831232L;

	/** 
	 * 被删除的推广组
	 */
	@ApiField("adgroup")
	private ADGroup adgroup;

	public void setAdgroup(ADGroup adgroup) {
		this.adgroup = adgroup;
	}
	public ADGroup getAdgroup( ) {
		return this.adgroup;
	}

}
