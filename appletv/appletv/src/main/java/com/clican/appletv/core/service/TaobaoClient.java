package com.clican.appletv.core.service;

import java.util.List;

import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;

public interface TaobaoClient {

	public TaobaoAccessToken getAccessToken(String code);

	public List<TaobaoCategory> getTopCategories();

	public List<TaobaoCategory> getCategories(Long parentId);

	public boolean addFavorite(Integer itemtype, Integer isMall, Integer isLp,
			Integer isTaohua, Long id,String cookie);
}
