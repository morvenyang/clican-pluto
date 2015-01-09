package com.chinatelecom.xysq.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.bean.SuggestionBean;
import com.chinatelecom.xysq.enumeration.InnerModule;
import com.chinatelecom.xysq.enumeration.PosterType;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.util.StringUtils;

@Scope(ScopeType.PAGE)
@Name("posterAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class PosterAction extends PageListAction<Poster> {

	@In(required = true)
	FacesMessages statusMessages;

	private PageListDataModel<Poster> posterDataModel;

	private List<Area> areaTrees;

	private Poster poster;

	private List<PosterType> posterTypes;

	private List<InnerModule> innerModules;

	private Long selectedStoreId;

	private String selectedStoreName;

	public void listPosters() {
		posterTypes = PosterType.getPosterTypes();
		innerModules = InnerModule.getInnerModules();
		this.refresh();
	}

	private void refresh() {
		posterDataModel = new PageListDataModel<Poster>(PAGE_SIZE) {
			@Override
			public PageList<Poster> fetchPage(int page, int pageSize) {
				return getPosterService().findPosterByOwner(
						getIdentity().getUser(), 1, PAGE_SIZE);
			}
		};
	}

	public synchronized void uploadImage(UploadEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("upload image for poster");
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
			poster.setImage(image);
		} catch (IOException e) {
			log.error("", e);
		}
	}
	public void addPoster() {
		this.poster = new Poster();
		this.poster.setOwner(this.getIdentity().getUser());
		this.poster.setType(PosterType.HTML5);
	}

	public List<SuggestionBean> autoCompleteStores(Object suggest) {
		List<Store> stores = this.getStoreService().findStores(
				this.getIdentity().getUser().getId(), (String) suggest);
		List<SuggestionBean> suggestionBeans = new ArrayList<SuggestionBean>();
		for (Store store : stores) {
			SuggestionBean ps = new SuggestionBean(store.getId(),
					store.getName());
			suggestionBeans.add(ps);
		}
		return suggestionBeans;

	}

	public void editPoster(Poster poster) {
		this.poster = this.getPosterService().findPosterById(poster.getId());
		if (this.poster.getType() == PosterType.STORE_DETAIL) {
			this.selectedStoreId = this.poster.getStore().getId();
			this.selectedStoreName = this.poster.getStore().getName();
		}
	}

	public void savePoster() {
		if (poster.getType() == PosterType.STORE_DETAIL) {
			if (this.selectedStoreId == null) {
				this.statusMessages.addToControl("posterStoreName",
						Severity.ERROR, "请选择正确的商家");
				return;
			} else {
				Store store = this.getStoreService().findStoreById(
						this.selectedStoreId);
				if (store == null) {
					this.statusMessages.addToControl("posterStoreName",
							Severity.ERROR, "请选择正确的商家");
					return;
				}

				poster.setStore(store);
			}
		}
		this.getPosterService().savePoster(poster);
		this.refresh();
	}

	public PageListDataModel<Poster> getPosterDataModel() {
		return posterDataModel;
	}

	public void setPosterDataModel(PageListDataModel<Poster> posterDataModel) {
		this.posterDataModel = posterDataModel;
	}

	public List<Area> getAreaTrees() {
		return areaTrees;
	}

	public void setAreaTrees(List<Area> areaTrees) {
		this.areaTrees = areaTrees;
	}

	public Poster getPoster() {
		return poster;
	}

	public void setPoster(Poster poster) {
		this.poster = poster;
	}

	public List<PosterType> getPosterTypes() {
		return posterTypes;
	}

	public void setPosterTypes(List<PosterType> posterTypes) {
		this.posterTypes = posterTypes;
	}

	@Override
	public PageListDataModel<Poster> getDefaultDataModel() {
		return posterDataModel;
	}

	public Long getSelectedStoreId() {
		return selectedStoreId;
	}

	public void setSelectedStoreId(Long selectedStoreId) {
		this.selectedStoreId = selectedStoreId;
	}

	public String getSelectedStoreName() {
		return selectedStoreName;
	}

	public void setSelectedStoreName(String selectedStoreName) {
		this.selectedStoreName = selectedStoreName;
	}

	public List<InnerModule> getInnerModules() {
		return innerModules;
	}

	public void setInnerModules(List<InnerModule> innerModules) {
		this.innerModules = innerModules;
	}

}
