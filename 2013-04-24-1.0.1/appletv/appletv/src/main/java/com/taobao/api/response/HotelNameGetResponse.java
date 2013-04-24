package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Hotel;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.hotel.name.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class HotelNameGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4885421974861351396L;

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
