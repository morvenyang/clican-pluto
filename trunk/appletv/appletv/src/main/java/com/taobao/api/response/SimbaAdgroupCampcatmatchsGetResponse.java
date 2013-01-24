package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.ADGroupCatMatchPage;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroup.campcatmatchs.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupCampcatmatchsGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3293882318932865337L;

	/** 
	 * 一页推广组类目出价列表
	 */
	@ApiField("adgroupcatmatchs")
	private ADGroupCatMatchPage adgroupcatmatchs;

	public void setAdgroupcatmatchs(ADGroupCatMatchPage adgroupcatmatchs) {
		this.adgroupcatmatchs = adgroupcatmatchs;
	}
	public ADGroupCatMatchPage getAdgroupcatmatchs( ) {
		return this.adgroupcatmatchs;
	}

}
