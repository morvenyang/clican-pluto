package com.clican.pluto.cms.ui.action;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.ui.ext.security.Identity;

@Scope(ScopeType.STATELESS)
@Name("authenticator")
public class AuthenticatorAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8711495324996368066L;

	private final static Log log = LogFactory.getLog(AuthenticatorAction.class);

	@In
	private Identity identity;

	public boolean authenticate() {
		String username = identity.getCredentials().getUsername();
		if (log.isDebugEnabled()) {
			log.debug("username[" + username + "]");
		}
		HttpSession session = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getSession();
		String jsessionid = session.getId();
		identity.setJsessionid(jsessionid);
		return true;
	}

}
