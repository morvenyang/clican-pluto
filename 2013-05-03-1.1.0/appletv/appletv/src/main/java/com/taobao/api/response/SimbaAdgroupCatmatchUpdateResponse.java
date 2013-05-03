package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.ADGroupCatmatch;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroup.catmatch.update response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupCatmatchUpdateResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2551442445154783792L;

	/** 
	 * 推广组的类目出价对象
	 */
	@ApiField("adgroupcatmatch")
	private ADGroupCatmatch adgroupcatmatch;

	public void setAdgroupcatmatch(ADGroupCatmatch adgroupcatmatch) {
		this.adgroupcatmatch = adgroupcatmatch;
	}
	public ADGroupCatmatch getAdgroupcatmatch( ) {
		return this.adgroupcatmatch;
	}

}
