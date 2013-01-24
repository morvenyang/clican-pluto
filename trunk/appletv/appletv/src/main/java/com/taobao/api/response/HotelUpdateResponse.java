package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Hotel;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.hotel.update response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class HotelUpdateResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2465996653672411237L;

	/** 
	 * 酒店结构
	 */
	@ApiField("hotel")
	private Hotel hotel;

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	public Hotel getHotel( ) {
		return this.hotel;
	}

}
