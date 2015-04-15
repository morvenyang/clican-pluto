package com.chinatelecom.xysq.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.model.Award;
import com.chinatelecom.xysq.model.AwardStoreRel;
import com.chinatelecom.xysq.model.Store;

@Scope(ScopeType.PAGE)
@Name("awardAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class AwardAction extends BaseAction {

	private final static Log log = LogFactory.getLog(AwardAction.class);

	@In(required = true)
	FacesMessages statusMessages;

	private Award award;

	private List<Award> awards;

	private PageListDataModel<Store> storeDataModel;

	private List<AwardStoreRel> awardStoreRels;

	public void listAwards() {
		this.awards = this.getAwardService().findAllAwards();
	}

	public void addAward() {
		this.award = new Award();
		this.awardStoreRels = new ArrayList<AwardStoreRel>();
	}

	public void editAward(Award award) {
		this.award = this.getAwardService().findAwardById(award.getId());
		this.awardStoreRels = new ArrayList<AwardStoreRel>(this.award.getAwardStoreRelSet());
	}

	public void activeAward(Award award) {
		award.setActive(true);
		this.getAwardService().saveAward(award);
		this.awards = this.getAwardService().findAllAwards();
	}

	public void inactiveAward(Award award) {
		award.setActive(false);
		this.getAwardService().saveAward(award);
		this.awards = this.getAwardService().findAllAwards();
	}

	public void deleteAward(Award award) {
		this.getAwardService().deleteAward(award);
		this.awards = this.getAwardService().findAllAwards();
	}

	public void saveAward() {
		this.award.setAwardStoreRelSet(new HashSet<AwardStoreRel>(this.awardStoreRels));
		this.getAwardService().saveAward(this.award);
		this.awards = this.getAwardService().findAllAwards();
	}

	public void prepareStores() {
		storeDataModel = new PageListDataModel<Store>(25) {
			@Override
			public PageList<Store> fetchPage(int page, int pageSize) {
				return getStoreService().findStoreByOwner(null, 1, pageSize);
			}
		};
	}

	public void selectStore(Store store){
		AwardStoreRel asr = new AwardStoreRel();
		asr.setStore(store);
		asr.setAward(this.award);
		awardStoreRels.add(asr);
	}
	public void deleteAwardStoreRel(AwardStoreRel awardStoreRel) {
		awardStoreRels.remove(awardStoreRel);
	}


	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public PageListDataModel<Store> getStoreDataModel() {
		return storeDataModel;
	}

	public void setStoreDataModel(PageListDataModel<Store> storeDataModel) {
		this.storeDataModel = storeDataModel;
	}

	public int getStorePage() {
		return this.getStoreDataModel().getRowIndex() / 25 + 1;
	}

	public void setStorePage(int page) {
		this.getStoreDataModel().setRowIndex((page - 1) * 25);
	}

	public List<AwardStoreRel> getAwardStoreRels() {
		return awardStoreRels;
	}

	public void setAwardStoreRels(List<AwardStoreRel> awardStoreRels) {
		this.awardStoreRels = awardStoreRels;
	}

}
