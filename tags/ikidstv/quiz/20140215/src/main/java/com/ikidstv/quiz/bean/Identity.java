package com.ikidstv.quiz.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import com.ikidstv.quiz.model.User;

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

    private List<String> menuList;

    private List<String> selectedMenuList;

    private Map<String, String> userConfigurationMap;

    private Integer menuStyle;

    // 是否可以查看临时发布的报告.
    private boolean queryTempReleaseReport;

    private int pageSize;
    private int reportPageSize;
    private int portfolioDetailPageSize;

    private MenuNavigation menuNavigation;

    private boolean autoLogin;

    /**
     * 是否配置了角色和报告类型的关系
     */
    private boolean limitReportType;

    /**
     * 3级报告类型id
     */
    private Set<Long> allowedReportTypeIdSet;

    /**
     * 用户登录的IP地址
     */
    private String loginIp;
    /**
     * 用户登录时的验证码,是否需要验证
     */
    private String loginSmsMark;

    private boolean showPromptAfterLogin = true;
    
    private boolean ie = false;

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

    @BypassInterceptors
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @BypassInterceptors
    public List<String> getMenuList() {
        if (menuList == null) {
            menuList = new ArrayList<String>();
        }
        return menuList;
    }

    public void setMenuList(List<String> menuList) {
        this.menuList = menuList;
    }

    @BypassInterceptors
    public List<String> getSelectedMenuList() {
        return selectedMenuList;
    }

    public void setSelectedMenuList(List<String> selectedMenuList) {
        this.selectedMenuList = selectedMenuList;
    }

    @BypassInterceptors
    public Map<String, String> getUserConfigurationMap() {
        return userConfigurationMap;
    }

    public void setUserConfigurationMap(Map<String, String> userConfigurationMap) {
        this.userConfigurationMap = userConfigurationMap;
    }

    @BypassInterceptors
    public boolean isLimitReportType() {
        return limitReportType;
    }

    public void setLimitReportType(boolean limitReportType) {
        this.limitReportType = limitReportType;
    }

    @BypassInterceptors
    public Set<Long> getAllowedReportTypeIdSet() {
        return allowedReportTypeIdSet;
    }

    public void setAllowedReportTypeIdSet(Set<Long> allowedReportTypeIdSet) {
        this.allowedReportTypeIdSet = allowedReportTypeIdSet;
    }

    @BypassInterceptors
    public MenuNavigation getMenuNavigation() {
        if (menuNavigation == null) {
            menuNavigation = new MenuNavigation();
        }
        return menuNavigation;
    }

    public void setMenuNavigation(MenuNavigation menuNavigation) {
        this.menuNavigation = menuNavigation;
    }

    public void setMenuStyle(Integer menuStyle) {
        this.menuStyle = menuStyle;
    }

    @BypassInterceptors
    public Integer getMenuStyle() {
        return menuStyle;
    }

    @BypassInterceptors
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @BypassInterceptors
    public int getReportPageSize() {
        return reportPageSize;
    }

    public void setReportPageSize(int reportPageSize) {
        this.reportPageSize = reportPageSize;
    }

    @BypassInterceptors
    public String getLoginSmsMark() {
        return loginSmsMark;
    }

    public void setLoginSmsMark(String loginSmsMark) {
        this.loginSmsMark = loginSmsMark;
    }

    public void setPortfolioDetailPageSize(int portfolioDetailPageSize) {
        this.portfolioDetailPageSize = portfolioDetailPageSize;
    }

    @BypassInterceptors
    public int getPortfolioDetailPageSize() {
        return portfolioDetailPageSize;
    }

    @BypassInterceptors
    public boolean isShowPromptAfterLogin() {
        return showPromptAfterLogin;
    }

    public void setShowPromptAfterLogin(boolean showPromptAfterLogin) {
        this.showPromptAfterLogin = showPromptAfterLogin;
    }

    @BypassInterceptors
    public boolean isQueryTempReleaseReport() {
        return queryTempReleaseReport;
    }

    public void setQueryTempReleaseReport(boolean queryTempReleaseReport) {
        this.queryTempReleaseReport = queryTempReleaseReport;
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

	public boolean isIe() {
		return ie;
	}

	public void setIe(boolean ie) {
		this.ie = ie;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Log getLog() {
		return log;
	}

}
