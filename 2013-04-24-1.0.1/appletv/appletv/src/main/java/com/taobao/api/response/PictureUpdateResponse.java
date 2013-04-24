package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.picture.update response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class PictureUpdateResponse extends TaobaoResponse {

	private static final long serialVersionUID = 6237752324232352621L;

	/** 
	 * 更新是否成功
	 */
	@ApiField("done")
	private Boolean done;

	public void setDone(Boolean done) {
		this.done = done;
	}
	public Boolean getDone( ) {
		return this.done;
	}

}
