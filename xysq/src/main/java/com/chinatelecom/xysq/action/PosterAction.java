package com.chinatelecom.xysq.action;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.enumeration.PosterType;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.Store;

@Scope(ScopeType.PAGE)
@Name("posterAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class PosterAction extends PageListAction<Poster> {

	private PageListDataModel<Poster> posterDataModel;

	private List<Area> areaTrees;

	private Poster poster;
	
	private List<PosterType> posterTypes;

	public void listPosters() {
		posterTypes = PosterType.getPosterTypes();
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

	public void addPoster(){
		this.poster = new Poster();
		this.poster.setType(PosterType.HTML5);
	}
	
	public List<Store> autoCompleteStores(Object suggest){
		List<Store> stores = this.getStoreService().findStores(this.getIdentity().getUser().getId(), (String)suggest);
		return stores;
	}
	
	public void editPoster(Poster poster){
		this.poster = this.getPosterService().findPosterById(poster.getId());
	}
	
	public void savePoster(){
		this.getPosterService().savePoster(poster);
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
	
	
}
