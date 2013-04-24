package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.ump.details.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UmpDetailsGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4382377775586523694L;

	/** 
	 * 活动详情的信息
	 */
	@ApiListField("contents")
	@ApiField("string")
	private List<String> contents;

	/** 
	 * 记录总数
	 */
	@ApiField("total_count")
	private Long totalCount;

	public void setContents(List<String> contents) {
		this.contents = contents;
	}
	public List<String> getContents( ) {
		return this.contents;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public Long getTotalCount( ) {
		return this.totalCount;
	}

}
