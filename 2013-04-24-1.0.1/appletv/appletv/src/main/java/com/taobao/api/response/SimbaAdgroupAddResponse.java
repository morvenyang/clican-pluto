package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.ADGroup;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroup.add response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupAddResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2793975536866188586L;

	/** 
	 * 新增加的推广组
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
