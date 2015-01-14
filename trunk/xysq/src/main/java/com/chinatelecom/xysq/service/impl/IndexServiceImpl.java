package com.chinatelecom.xysq.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.dao.AnnouncementAndNoticeDao;
import com.chinatelecom.xysq.dao.PosterDao;
import com.chinatelecom.xysq.json.AnnouncementAndNoticeJson;
import com.chinatelecom.xysq.json.IndexJson;
import com.chinatelecom.xysq.json.PosterJson;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.service.IndexService;

public class IndexServiceImpl implements IndexService {

	private PosterDao posterDao;

	private SpringProperty springProperty;

	private AnnouncementAndNoticeDao announcementAndNoticeDao;

	public void setPosterDao(PosterDao posterDao) {
		this.posterDao = posterDao;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@Override
	public String queryIndex(Long communityId) {
		List<Poster> posters = null;
		if (communityId == null) {
			posters = posterDao.queryDefaultPoster();
		} else {
			posters = posterDao.queryPoster(communityId);
		}
		if (posters.size() == 0) {
			posters = posterDao.queryDefaultPoster();
		}
		List<PosterJson> posterJsons = new ArrayList<PosterJson>();
		for (Poster p : posters) {
			PosterJson pj = new PosterJson();
			pj.setId(p.getId());
			pj.setHtml5Link(p.getHtml5Link());
			pj.setImagePath(springProperty.getServerUrl()
					+ springProperty.getContextPath() + "/image.do?imagePath="
					+ p.getImage().getPath());
			pj.setInnerModule(p.getInnerModule());
			pj.setName(p.getName());
			if (p.getStore() != null) {
				pj.setStoreId(p.getStore().getId());
			}
			pj.setType(p.getType());
			posterJsons.add(pj);
		}
		IndexJson indexJson = new IndexJson();
		indexJson.setPosters(posterJsons);
		return JSONObject.fromObject(indexJson).toString();
	}

	@Override
	public String queryAnnouncementAndNotice(Long communityId,
			boolean announcement, int page, int pageSize) {
		List<AnnouncementAndNotice> list = announcementAndNoticeDao
				.findAnnouncementAndNotice(communityId, announcement, page,
						pageSize);
		List<AnnouncementAndNoticeJson> jsonList = new ArrayList<AnnouncementAndNoticeJson>();
		for (AnnouncementAndNotice aan : list) {
			AnnouncementAndNoticeJson json = new AnnouncementAndNoticeJson();
			json.setId(aan.getId());
			json.setContent(aan.getContent());
			json.setCreateTime(aan.getCreateTime());
			json.setModifyTime(aan.getModifyTime());
			json.setNoticeCategory(aan.getNoticeCategory());
			json.setTitle(aan.getTitle());
			jsonList.add(json);
		}
		return JSONArray.fromObject(jsonList).toString();
	}

}
