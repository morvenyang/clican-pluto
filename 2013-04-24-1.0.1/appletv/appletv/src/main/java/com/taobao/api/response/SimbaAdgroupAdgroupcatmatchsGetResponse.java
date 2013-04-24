package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.ADGroupCatmatch;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.adgroup.adgroupcatmatchs.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaAdgroupAdgroupcatmatchsGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3382553114194519786L;

	/** 
	 * 类目出价列表
	 */
	@ApiListField("adgroup_catmatch_list")
	@ApiField("a_d_group_catmatch")
	private List<ADGroupCatmatch> adgroupCatmatchList;

	public void setAdgroupCatmatchList(List<ADGroupCatmatch> adgroupCatmatchList) {
		this.adgroupCatmatchList = adgroupCatmatchList;
	}
	public List<ADGroupCatmatch> getAdgroupCatmatchList( ) {
		return this.adgroupCatmatchList;
	}

}
