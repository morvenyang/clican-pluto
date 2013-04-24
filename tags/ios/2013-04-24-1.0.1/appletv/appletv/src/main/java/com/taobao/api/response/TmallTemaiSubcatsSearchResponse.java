package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.TmallTmCat;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: tmall.temai.subcats.search response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TmallTemaiSubcatsSearchResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2821176383322299186L;

	/** 
	 * 返回传入类目下子类目信息
	 */
	@ApiListField("cat_list")
	@ApiField("tmall_tm_cat")
	private List<TmallTmCat> catList;

	public void setCatList(List<TmallTmCat> catList) {
		this.catList = catList;
	}
	public List<TmallTmCat> getCatList( ) {
		return this.catList;
	}

}
