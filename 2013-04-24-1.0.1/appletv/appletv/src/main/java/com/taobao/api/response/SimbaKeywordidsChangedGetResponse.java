package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.keywordids.changed.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaKeywordidsChangedGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5417882619661358927L;

	/** 
	 * 词的ID列表
	 */
	@ApiListField("changed_keyword_ids")
	@ApiField("number")
	private List<Long> changedKeywordIds;

	public void setChangedKeywordIds(List<Long> changedKeywordIds) {
		this.changedKeywordIds = changedKeywordIds;
	}
	public List<Long> getChangedKeywordIds( ) {
		return this.changedKeywordIds;
	}

}
