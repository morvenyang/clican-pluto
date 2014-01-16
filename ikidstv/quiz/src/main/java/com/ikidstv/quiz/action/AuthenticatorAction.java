package com.ikidstv.quiz.action;

import java.util.HashSet;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;

import com.ikidstv.quiz.bean.Identity;
import com.ikidstv.quiz.model.User;

@Scope(ScopeType.STATELESS)
@Name("authenticator")
public class AuthenticatorAction extends SpringBeanAction {

	@In
	private Identity identity;

	@In(required = true)
	FacesMessages statusMessages;

	public boolean authenticate() {
		String username = identity.getCredentials().getUsername();
		User user = getUserService().login(username,
				identity.getCredentials().getPassword());
		if (user==null) {
			statusMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,
					"loginFailure");
			return false;
		}else{
			String userAgent= FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("User-Agent");
			if(StringUtils.isNotEmpty(userAgent)&&userAgent.indexOf("MSIE")!=-1){
				identity.setIe(true);
			}else{
				identity.setIe(false);
			}
			identity.setUser(user);
			Set<String> permissionSet = new HashSet<String>();
			identity.setPermissionSet(permissionSet);
			if(user.getRole()==2){//admin
				permissionSet.add("admin");
			}else{//teacher
				permissionSet.add("teacher");
			}
			return true;
		}
	}

}
