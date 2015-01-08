package com.chinatelecom.xysq.service;

import java.util.List;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.User;

public interface StoreService {

	public PageList<Store> findStoreByOwner(User owner, int page, int pageSize);

	public List<Store> findStores(Long ownerId, String keyword);

	public void saveStore(Store store);

	public void deleteStore(Store store);

	public Store findStoreById(Long id);
}
