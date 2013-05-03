package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.ReferenceDetail;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.picture.referenced.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class PictureReferencedGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2869118637213471624L;

	/** 
	 * 引用详情列表
	 */
	@ApiListField("references")
	@ApiField("reference_detail")
	private List<ReferenceDetail> references;

	public void setReferences(List<ReferenceDetail> references) {
		this.references = references;
	}
	public List<ReferenceDetail> getReferences( ) {
		return this.references;
	}

}
