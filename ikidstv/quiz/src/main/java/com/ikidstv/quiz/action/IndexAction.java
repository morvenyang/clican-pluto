package com.ikidstv.quiz.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;


@Scope(ScopeType.PAGE)
@Name("indexAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class IndexAction {

	public String welcome(){
		return "";
	}
}
