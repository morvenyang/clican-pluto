package com.taobao.api.response;

import java.util.Date;
import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.feedback.add response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class FeedbackAddResponse extends TaobaoResponse {

	private static final long serialVersionUID = 6521886519461929524L;

	/** 
	 * 返回记录的时间
	 */
	@ApiField("result")
	private Date result;

	public void setResult(Date result) {
		this.result = result;
	}
	public Date getResult( ) {
		return this.result;
	}

}
