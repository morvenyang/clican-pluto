package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroupids.changed.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupidsChangedGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7152111266121953896L;

	/** 
	 * 推广组ID列表
	 */
	@ApiListField("changed_adgroupids")
	@ApiField("number")
	private List<Long> changedAdgroupids;

	public void setChangedAdgroupids(List<Long> changedAdgroupids) {
		this.changedAdgroupids = changedAdgroupids;
	}
	public List<Long> getChangedAdgroupids( ) {
		return this.changedAdgroupids;
	}

}
