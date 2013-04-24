package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.CreativeRecord;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.creative.update response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCreativeUpdateResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7653546229957781796L;

	/** 
	 * 创意修改记录对象
	 */
	@ApiField("creativerecord")
	private CreativeRecord creativerecord;

	public void setCreativerecord(CreativeRecord creativerecord) {
		this.creativerecord = creativerecord;
	}
	public CreativeRecord getCreativerecord( ) {
		return this.creativerecord;
	}

}
