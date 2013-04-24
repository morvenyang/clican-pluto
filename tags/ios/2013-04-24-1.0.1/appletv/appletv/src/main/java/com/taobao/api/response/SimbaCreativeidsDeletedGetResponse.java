package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.creativeids.deleted.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCreativeidsDeletedGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4247417334769771938L;

	/** 
	 * 创意ID列表
	 */
	@ApiListField("deleted_creative_ids")
	@ApiField("number")
	private List<Long> deletedCreativeIds;

	public void setDeletedCreativeIds(List<Long> deletedCreativeIds) {
		this.deletedCreativeIds = deletedCreativeIds;
	}
	public List<Long> getDeletedCreativeIds( ) {
		return this.deletedCreativeIds;
	}

}
