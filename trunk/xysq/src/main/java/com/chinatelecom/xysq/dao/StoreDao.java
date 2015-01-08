package com.chinatelecom.xysq.dao;

import java.util.List;
import java.util.Set;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.User;

public interface StoreDao {

	public PageList<Store> findStoreByOwner(User owner,int page, int pageSize);
	
	public List<Store> findStores(Long ownerId, String keyword);
	
	public Store findStoreById(Long id);

	public void saveStore(Store store);

	public void saveImage(Image image);

	public void deleteStoreImage(Store store,Set<Long> notInImageIds);
	
	public void deleteStore(Store store);

}
