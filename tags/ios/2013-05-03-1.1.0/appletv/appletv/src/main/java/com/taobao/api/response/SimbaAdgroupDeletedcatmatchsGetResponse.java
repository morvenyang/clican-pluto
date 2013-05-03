package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.ADGroupCatMatchPage;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroup.deletedcatmatchs.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupDeletedcatmatchsGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5723692925575141429L;

	/** 
	 * 一页类目出价对象
	 */
	@ApiField("deleted_catmatchs")
	private ADGroupCatMatchPage deletedCatmatchs;

	public void setDeletedCatmatchs(ADGroupCatMatchPage deletedCatmatchs) {
		this.deletedCatmatchs = deletedCatmatchs;
	}
	public ADGroupCatMatchPage getDeletedCatmatchs( ) {
		return this.deletedCatmatchs;
	}

}
