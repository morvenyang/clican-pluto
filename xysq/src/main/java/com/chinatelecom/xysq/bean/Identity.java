package com.chinatelecom.xysq.bean;

import java.util.Set;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.Seam;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Events;
import org.jboss.seam.web.Session;

import com.chinatelecom.xysq.model.User;

@Name("org.jboss.seam.security.identity")
@Scope(ScopeType.SESSION)
@Install(precedence = Install.APPLICATION)
@Startup
public class Identity extends org.jboss.seam.security.Identity {

    /**
     * 
     */
    private static final long serialVersionUID = 5423746160747703118L;

    private final static Log log = LogFactory.getLog(Identity.class);

    private User user;

    private Set<String> permissionSet;

    private String jsessionid;

    private boolean autoLogin;

    @BypassInterceptors
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @BypassInterceptors
    public Set<String> getPermissionSet() {
        return permissionSet;
    }

    public void setPermissionSet(Set<String> permissionSet) {
        this.permissionSet = permissionSet;
    }

    @BypassInterceptors
    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    @BypassInterceptors
    @Override
    public boolean hasPermission(String name, String action, Object... arg) {
        if (StringUtils.isNotEmpty(name)) {
            if (permissionSet != null) {
                boolean havePermission = false;
                for (String permission : name.split(",")) {
                    havePermission = permissionSet.contains(permission);
                    if (havePermission) {
                        break;
                    }
                }
                return havePermission;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @BypassInterceptors
    @Override
    public boolean hasPermission(Object target, String action) {
        if (target != null && StringUtils.isNotEmpty(target.toString())) {
            if (permissionSet != null) {
                boolean havePermission = false;
                for (String permission : target.toString().split(",")) {
                    havePermission = permissionSet.contains(permission);
                    if (havePermission) {
                        break;
                    }
                }
                return havePermission;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void quietLogin() {
        this.setAutoLogin(true);
        this.getCredentials().getUsername();
        super.quietLogin();
    }

    public String login() {
        try {
            if (isLoggedIn()) {
                String username = this.getCredentials().getUsername();
                String password = this.getCredentials().getPassword();
                this.logout();
                this.getCredentials().setUsername(username);
                this.getCredentials().setPassword(password);
                ScopeType.SESSION.getContext().remove(Seam.getComponentName(Session.class));
                Session.instance();
            }
            authenticate();
            if (!isLoggedIn()) {
                throw new LoginException();
            }
            if (log.isDebugEnabled()) {
                log.debug("Login successful for: " + getCredentials().getUsername());
            }
            if (Events.exists())
                Events.instance().raiseEvent(EVENT_LOGIN_SUCCESSFUL);
            return "loggedIn";
        } catch (LoginException ex) {
            this.getCredentials().invalidate();
            if (log.isDebugEnabled()) {
                log.debug("Login failed for: " + getCredentials().getUsername(), ex);
            }
            if (Events.exists())
                Events.instance().raiseEvent(EVENT_LOGIN_FAILED, ex);
        }

        return null;
    }

   



    @Override
    public boolean isLoggedIn(boolean attemptLogin) {
        if (attemptLogin) {
            this.getCredentials().getUsername();
            return tryLogin();
        } else {
            return isLoggedIn();
        }
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

}
