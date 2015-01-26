package com.chinatelecom.xysq.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.StoreDao;
import com.chinatelecom.xysq.json.StoreJson;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.StoreService;
import com.chinatelecom.xysq.util.DateJsonValueProcessor;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class StoreServiceImpl implements StoreService {

	private StoreDao storeDao;

	public void setStoreDao(StoreDao storeDao) {
		this.storeDao = storeDao;
	}

	@Override
	public PageList<Store> findStoreByOwner(User owner, int page, int pageSize) {
		return storeDao.findStoreByOwner(owner, page, pageSize);
	}

	@Override
	public List<Store> findStores(Long ownerId, String keyword) {
		return storeDao.findStores(ownerId, keyword);
	}

	@Override
	public void saveStore(Store store) {
		Set<Long> notInImageIds = new HashSet<Long>();
		for (Image image : store.getImages()) {
			if (image.getId() != null) {
				notInImageIds.add(image.getId());
			}
		}
		if (store.getId() != null) {
			this.storeDao.deleteStoreImage(store, notInImageIds);
		}
		store.setShortPinyin(PinyinHelper.getShortPinyin(store.getName()));
		store.setPinyin(PinyinHelper.convertToPinyinString(store.getName(), ""));
		this.storeDao.saveStore(store);
		for (Image image : store.getImages()) {
			image.setStore(store);
			this.storeDao.saveImage(image);
		}
	}

	@Override
	public void deleteStore(Store store) {
		this.storeDao.deleteStore(store);
	}

	@Override
	public Store findStoreById(Long id) {
		return this.storeDao.findStoreById(id);
	}

	@Override
	public String queryStore(Long storeId) {
		Store store = this.storeDao.findStoreById(storeId);
		StoreJson storeJson = new StoreJson();
		storeJson.setId(store.getId());
		storeJson.setAddress(store.getAddress());
		storeJson.setCreateTime(store.getCreateTime());
		storeJson.setDescription(store.getDescription());
		storeJson.setModifyTime(store.getModifyTime());
		storeJson.setName(store.getName());
		storeJson.setPinyin(store.getPinyin());
		storeJson.setShortPinyin(store.getShortPinyin());
		storeJson.setTel(store.getTel());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		return JSONObject.fromObject(storeJson, jsonConfig).toString();
	}

}
