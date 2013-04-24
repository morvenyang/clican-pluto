package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.User;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.user.buyer.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class UserBuyerGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5741897262736887438L;

	/** 
	 * 只返回user_id,nick,sex,buyer_credit,avatar,has_shop,vip_info参数
	 */
	@ApiField("user")
	private User user;

	public void setUser(User user) {
		this.user = user;
	}
	public User getUser( ) {
		return this.user;
	}

}
