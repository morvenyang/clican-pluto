package com.chinatelecom.xysq.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.util.StringUtils;

@Scope(ScopeType.PAGE)
@Name("storeAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class StoreAction extends PageListAction<Store> {

	private PageListDataModel<Store> storeDataModel;
	
	private Store store;
	
	private Store selectedStore;

	public void listStores() {
		this.refresh();
	}

	private void refresh() {
		storeDataModel = new PageListDataModel<Store>(PAGE_SIZE) {
			@Override
			public PageList<Store> fetchPage(int page, int pageSize) {
				return getStoreService().findStoreByOwner(
						getIdentity().getUser(), 1, PAGE_SIZE);
			}
		};
		
	}

	public void selectStore(Store store){
		this.selectedStore = store;
	}
	public void addStore() {
		this.store = new Store();
		this.store.setOwner(this.getIdentity().getUser());
		this.store.setImages(new ArrayList<Image>());
	}

	public void editStore(Store store) {
		this.store = this.getStoreService().findStoreById(store.getId());
	}

	public void saveStore() {
		this.getStoreService().saveStore(store);
		this.refresh();
	}

	public void deleteImage(Image image) {
		this.store.getImages().remove(image);
	}

	public void deleteStore(Store store) {
		this.getStoreService().deleteStore(store);
		this.refresh();
	}

	public synchronized void uploadImage(UploadEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("upload image for store");
		}
		List<UploadItem> itemList = event.getUploadItems();
		UploadItem item = itemList.get(0);
		File imageTempFile = item.getFile();
		try {
			byte[] data = FileUtils.readFileToByteArray(imageTempFile);
			String suffix = org.apache.commons.lang.StringUtils
					.substringAfterLast(item.getFileName(), ".");
			String filePath = StringUtils.generateFilePathByDate()
					+ UUID.randomUUID().toString() + "." + suffix;
			FileUtils.writeByteArrayToFile(new File(this.getSpringProperty()
					.getImageUrlPrefix() + "/" + filePath), data);
			Image image = new Image();
			image.setPath(filePath);
			image.setName(item.getFileName());
			image.setStore(store);
			image.setSeq(this.store.getImages().size() + 1);
			this.store.getImages().add(image);
		} catch (IOException e) {
			log.error("", e);
		}
	}

	public PageListDataModel<Store> getStoreDataModel() {
		return storeDataModel;
	}

	public void setStoreDataModel(PageListDataModel<Store> storeDataModel) {
		this.storeDataModel = storeDataModel;
	}

	@Override
	public PageListDataModel<Store> getDefaultDataModel() {
		return storeDataModel;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Store getSelectedStore() {
		return selectedStore;
	}

	public void setSelectedStore(Store selectedStore) {
		this.selectedStore = selectedStore;
	}

}
