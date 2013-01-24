package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.keywordids.deleted.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaKeywordidsDeletedGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4848783112917713336L;

	/** 
	 * 词ID列表
	 */
	@ApiListField("deleted_keyword_ids")
	@ApiField("number")
	private List<Long> deletedKeywordIds;

	public void setDeletedKeywordIds(List<Long> deletedKeywordIds) {
		this.deletedKeywordIds = deletedKeywordIds;
	}
	public List<Long> getDeletedKeywordIds( ) {
		return this.deletedKeywordIds;
	}

}
