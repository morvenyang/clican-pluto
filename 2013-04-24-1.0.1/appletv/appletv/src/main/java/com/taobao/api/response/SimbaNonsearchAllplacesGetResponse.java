package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.Place;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.simba.nonsearch.allplaces.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SimbaNonsearchAllplacesGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8511634941953873789L;

	/** 
	 * 定向推广投放位置列表
	 */
	@ApiListField("place_list")
	@ApiField("place")
	private List<Place> placeList;

	public void setPlaceList(List<Place> placeList) {
		this.placeList = placeList;
	}
	public List<Place> getPlaceList( ) {
		return this.placeList;
	}

}
