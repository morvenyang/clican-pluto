package com.peacebird.dataserver.action;

import java.util.HashSet;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;

import com.peacebird.dataserver.bean.Identity;
import com.peacebird.dataserver.model.User;

@Scope(ScopeType.STATELESS)
@Name("authenticator")
public class AuthenticatorAction extends BaseAction {

	@In
	private Identity identity;

	@In(required = true)
	FacesMessages statusMessages;

	public boolean authenticate() {
		String username = identity.getCredentials().getUsername();
		String hashPassword = DigestUtils.shaHex(identity.getCredentials().getPassword());
		User user = getUserService().findUserByUserName(username);
		if (user==null || !user.getPassword().equals(hashPassword)) {
			statusMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,
					"loginFailure");
			return false;
		}else{
			if(!user.isActive()){
				statusMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,
				"loginUserDisabled");
				return false;
			}
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
			}else{//client
				permissionSet.add("client");
			}
			return true;
		}
	}

}
