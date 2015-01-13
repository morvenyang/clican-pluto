package com.chinatelecom.xysq.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import com.chinatelecom.xysq.dao.PosterDao;
import com.chinatelecom.xysq.json.IndexJson;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.service.IndexService;

public class IndexServiceImpl implements IndexService {

	private PosterDao posterDao;

	@Override
	public String queryIndex(Long communityId) {
		List<Poster> posters = null;
		if (communityId == null) {
			posters = posterDao.queryDefaultPoster();
		} else {
			posters = posterDao.queryPoster(communityId);
		}
		IndexJson indexJson = new IndexJson();
		indexJson.setPosters(posters);
		return JSONObject.fromObject(indexJson).toString();
	}

}
