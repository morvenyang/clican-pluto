package com.chinatelecom.xysq.service.impl;

import java.util.HashSet;
import java.util.Set;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.StoreDao;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.StoreService;

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
	public void saveStore(Store store) {
		Set<Long> notInImageIds = new HashSet<Long>();
		for (Image image : store.getImages()) {
			if (image.getId() != null) {
				notInImageIds.add(image.getId());
			}
		}
		if (store.getId() != null) {
			this.storeDao.deleteStoreImage(store,notInImageIds);
		}
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

}
