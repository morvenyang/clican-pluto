package com.chinatelecom.xysq.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;

import com.chinatelecom.xysq.model.Award;


@Scope(ScopeType.PAGE)
@Name("awardAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class AwardAction extends BaseAction{

	private final static Log log = LogFactory.getLog(AwardAction.class);
	
	@In(required = true)
	FacesMessages statusMessages;
	
	private Award award;
	
	private List<Award> awards;
	
	public void listAwards() {
		this.awards = this.getAwardService().findAllAwards();
	}
	
	public void addAward(){
		this.award = new Award();
	}
	
	public void editAward(Award award){
		this.award = award;
	}
	public void activeAward(Award award){
		award.setActive(true);
		this.getAwardService().saveAward(award);
		this.awards = this.getAwardService().findAllAwards();
	}
	public void inactiveAward(Award award){
		award.setActive(false);
		this.getAwardService().saveAward(award);
		this.awards = this.getAwardService().findAllAwards();
	}
	public void deleteAward(Award award){
		this.getAwardService().deleteAward(award);
		this.awards = this.getAwardService().findAllAwards();
	}

	public void saveAward(){
		this.getAwardService().saveAward(award);
		this.awards = this.getAwardService().findAllAwards();
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
	
	
}
