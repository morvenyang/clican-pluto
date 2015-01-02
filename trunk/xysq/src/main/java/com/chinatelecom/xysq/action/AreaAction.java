package com.chinatelecom.xysq.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;


@Scope(ScopeType.PAGE)
@Name("areaAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class AreaAction extends BaseAction {

	
}
