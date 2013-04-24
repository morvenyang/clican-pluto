package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.increment.authorize.message.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class IncrementAuthorizeMessageGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7327459476889816847L;

	/** 
	 * 代表是否有下一页
	 */
	@ApiField("has_next")
	private Boolean hasNext;

	/** 
	 * 消息的json串
	 */
	@ApiListField("messages")
	@ApiField("string")
	private List<String> messages;

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}
	public Boolean getHasNext( ) {
		return this.hasNext;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	public List<String> getMessages( ) {
		return this.messages;
	}

}
