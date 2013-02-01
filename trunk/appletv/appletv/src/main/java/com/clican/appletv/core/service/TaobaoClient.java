package com.clican.appletv.core.service;

import java.util.List;
import java.util.Map;

import com.clican.appletv.core.model.TaobaoAccessToken;
import com.clican.appletv.core.model.TaobaoCategory;
import com.clican.appletv.core.model.TaobaoLove;
import com.clican.appletv.core.model.TaobaoLoveTag;

public interface TaobaoClient {

	public TaobaoAccessToken getAccessToken(String code);

	public List<TaobaoCategory> getTopCategories();

	public List<TaobaoCategory> getCategories(Long parentId);

	public List<Long> getItemsBySellerCategory(Long shopId, Long scid,
			String scname);

	public boolean addFavorite(Integer itemtype, Integer isMall, Integer isLp,
			Integer isTaohua, Long id, String cookie, String token);

	public Map<String, List<TaobaoLoveTag>> getTaobaoLoveTags();

	public List<TaobaoLove> queryTaobaoLoves(Long tagId);
}
