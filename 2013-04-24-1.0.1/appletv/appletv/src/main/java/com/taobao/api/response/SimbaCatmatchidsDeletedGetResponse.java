package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.catmatchids.deleted.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCatmatchidsDeletedGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 6465625239565821422L;

	/** 
	 * 类目出价ID列表
	 */
	@ApiListField("deleted_catmatch_ids")
	@ApiField("number")
	private List<Long> deletedCatmatchIds;

	public void setDeletedCatmatchIds(List<Long> deletedCatmatchIds) {
		this.deletedCatmatchIds = deletedCatmatchIds;
	}
	public List<Long> getDeletedCatmatchIds( ) {
		return this.deletedCatmatchIds;
	}

}
