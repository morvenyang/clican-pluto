package com.clican.appletv.core.service;

import com.clican.appletv.core.model.TaobaoAccessToken;

public interface TaobaoClient {

	public TaobaoAccessToken getAccessToken(String code);

}
