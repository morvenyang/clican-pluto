package com.chinatelecom.xysq.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.dao.AnnouncementAndNoticeDao;
import com.chinatelecom.xysq.dao.BroadbandRemindDao;
import com.chinatelecom.xysq.dao.PosterDao;
import com.chinatelecom.xysq.enumeration.NoticeCategory;
import com.chinatelecom.xysq.json.AnnouncementAndNoticeJson;
import com.chinatelecom.xysq.json.BroadbandRemindJson;
import com.chinatelecom.xysq.json.IndexJson;
import com.chinatelecom.xysq.json.PosterJson;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.BroadbandRemind;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.service.IndexService;
import com.chinatelecom.xysq.util.DateJsonValueProcessor;

public class IndexServiceImpl implements IndexService {

	private PosterDao posterDao;

	private SpringProperty springProperty;

	private AnnouncementAndNoticeDao announcementAndNoticeDao;

	private BroadbandRemindDao broadbandRemindDao;

	public void setPosterDao(PosterDao posterDao) {
		this.posterDao = posterDao;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setAnnouncementAndNoticeDao(
			AnnouncementAndNoticeDao announcementAndNoticeDao) {
		this.announcementAndNoticeDao = announcementAndNoticeDao;
	}

	public void setBroadbandRemindDao(BroadbandRemindDao broadbandRemindDao) {
		this.broadbandRemindDao = broadbandRemindDao;
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
			boolean announcement, NoticeCategory noticeCategory, int page,
			int pageSize) {
		List<AnnouncementAndNotice> list = announcementAndNoticeDao
				.findAnnouncementAndNotice(communityId, announcement,
						noticeCategory, page, pageSize);
		List<AnnouncementAndNoticeJson> jsonList = new ArrayList<AnnouncementAndNoticeJson>();
		for (AnnouncementAndNotice aan : list) {
			AnnouncementAndNoticeJson json = new AnnouncementAndNoticeJson();
			json.setId(aan.getId());
			json.setContent(aan.getContent());
			json.setCreateTime(aan.getCreateTime());
			json.setModifyTime(aan.getModifyTime());
			json.setNoticeCategory(aan.getNoticeCategory());
			json.setInnerModule(aan.getInnerModule());
			json.setTitle(aan.getTitle());
			jsonList.add(json);
		}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		return JSONArray.fromObject(jsonList, jsonConfig).toString();
	}

	@Override
	public String queryBroadbandRemind(String msisdn) {
		BroadbandRemindJson json = new BroadbandRemindJson();
		BroadbandRemind broadBandRemind = this.broadbandRemindDao
				.findBroadbandRemindByMsisdn(msisdn);
		if(broadBandRemind==null){
			json.setExist(false);
		}else{
			json.setExist(true);
			json.setBroadBandId(broadBandRemind.getBroadBandId());
			json.setExpiredDate(broadBandRemind.getExpiredDate());
			json.setMsisdn(broadBandRemind.getMsisdn());
			json.setUserName(broadBandRemind.getUserName());
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		return JSONObject.fromObject(json, jsonConfig).toString();
	}

}
