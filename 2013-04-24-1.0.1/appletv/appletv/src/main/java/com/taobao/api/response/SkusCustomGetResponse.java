package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.Sku;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.skus.custom.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SkusCustomGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7323449118627237995L;

	/** 
	 * Sku对象，具体字段以fields决定
	 */
	@ApiListField("skus")
	@ApiField("sku")
	private List<Sku> skus;

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}
	public List<Sku> getSkus( ) {
		return this.skus;
	}

}
