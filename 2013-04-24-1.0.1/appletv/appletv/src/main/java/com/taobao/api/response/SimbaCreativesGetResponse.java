package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.Creative;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.creatives.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaCreativesGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3562527122369178482L;

	/** 
	 * 创意对象列表
	 */
	@ApiListField("creatives")
	@ApiField("creative")
	private List<Creative> creatives;

	public void setCreatives(List<Creative> creatives) {
		this.creatives = creatives;
	}
	public List<Creative> getCreatives( ) {
		return this.creatives;
	}

}
