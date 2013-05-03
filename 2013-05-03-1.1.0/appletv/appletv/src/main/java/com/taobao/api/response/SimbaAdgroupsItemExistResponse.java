package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroups.item.exist response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupsItemExistResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2812973651133461234L;

	/** 
	 * true表示已经被推广，false表示没有被推广
	 */
	@ApiField("exist")
	private Boolean exist;

	public void setExist(Boolean exist) {
		this.exist = exist;
	}
	public Boolean getExist( ) {
		return this.exist;
	}

}
