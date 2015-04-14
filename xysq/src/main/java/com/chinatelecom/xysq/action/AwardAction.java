package com.chinatelecom.xysq.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;


@Scope(ScopeType.PAGE)
@Name("awardAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class AwardAction {

	@In(required = true)
	FacesMessages statusMessages;
	
	public void listAwards() {
		
	}
}
