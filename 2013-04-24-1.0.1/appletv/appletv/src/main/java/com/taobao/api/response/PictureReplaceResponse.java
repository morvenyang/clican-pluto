package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.picture.replace response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class PictureReplaceResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8269224868929616617L;

	/** 
	 * 图片替换是否成功
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
