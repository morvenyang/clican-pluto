package com.chinatelecom.xysq.action;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;

import com.chinatelecom.xysq.bean.Identity;
import com.chinatelecom.xysq.model.User;

@Scope(ScopeType.STATELESS)
@Name("authenticator")
public class AuthenticatorAction extends BaseAction {

	@In
	private Identity identity;

	@In(required = true)
	FacesMessages statusMessages;

	public boolean authenticate() {
		String username = identity.getCredentials().getUsername();
		String hashPassword = DigestUtils.shaHex(identity.getCredentials()
				.getPassword());
		User user = getUserService().findUserByUserName(username);
		if (user == null || !user.getPassword().equals(hashPassword)) {
			statusMessages.addFromResourceBundle(StatusMessage.Severity.ERROR,
					"loginFailure");
			return false;
		} else {
			if (!user.isActive()) {
				statusMessages.addFromResourceBundle(
						StatusMessage.Severity.ERROR, "loginUserDisabled");
				return false;
			}
			identity.setUser(user);
			Set<String> permissionSet = new HashSet<String>();
			identity.setPermissionSet(permissionSet);
			permissionSet.add(user.getRole().name());
			return true;
		}
	}

}
