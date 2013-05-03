package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.creativeids.changed.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCreativeidsChangedGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4153493462691589881L;

	/** 
	 * 创意ID列表
	 */
	@ApiListField("changed_creative_ids")
	@ApiField("number")
	private List<Long> changedCreativeIds;

	public void setChangedCreativeIds(List<Long> changedCreativeIds) {
		this.changedCreativeIds = changedCreativeIds;
	}
	public List<Long> getChangedCreativeIds( ) {
		return this.changedCreativeIds;
	}

}
