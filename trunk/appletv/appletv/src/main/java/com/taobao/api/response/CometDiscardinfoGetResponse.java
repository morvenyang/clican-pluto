package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.DiscardInfo;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.comet.discardinfo.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class CometDiscardinfoGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5213286248377211417L;

	/** 
	 * 丢弃消息的列表
	 */
	@ApiListField("discard_info_list")
	@ApiField("discard_info")
	private List<DiscardInfo> discardInfoList;

	public void setDiscardInfoList(List<DiscardInfo> discardInfoList) {
		this.discardInfoList = discardInfoList;
	}
	public List<DiscardInfo> getDiscardInfoList( ) {
		return this.discardInfoList;
	}

}
