package com.ikidstv.quiz.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.security.Restrict;

@Scope(ScopeType.SESSION)
@Name("ui")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class UIAction extends BaseAction{

    private String selectedPanel = "";      // 当前选中的菜单

    private String showLeftMenu = "";       // 是否显示左菜单

    private String showHeader = "";         // 是否显示头部

    private String width = "100%";          // UI界面的宽度
    private String height;                  // UI界面的高度
    private String menuWidth;               // 左侧菜单宽度
    private String wsWidth;                 // 工作空间宽度
    private String wsHeight = "100%";      // 工作空间高度 = 左侧菜单高度

    @BypassInterceptors
    public String getSelectedPanel() {
        return selectedPanel;
    }

    @BypassInterceptors
    public String getShowLeftMenu() {
        return showLeftMenu;
    }

    public void setShowLeftMenu(String showLeftMenu) {
        this.showLeftMenu = showLeftMenu;
    }

    @BypassInterceptors
    public String getShowHeader() {
        return showHeader;
    }

    public void setShowHeader(String showHeader) {
        this.showHeader = showHeader;
    }

    @BypassInterceptors
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @BypassInterceptors
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @BypassInterceptors
    public String getMenuWidth() {
        return menuWidth;
    }

    public void setMenuWidth(String menuWidth) {
        this.menuWidth = menuWidth;
    }

    @BypassInterceptors
    public String getWsWidth() {
        return wsWidth;
    }

    public void setWsWidth(String wsWidth) {
        this.wsWidth = wsWidth;
    }

    public String getWsHeight() {
        return wsHeight;
    }

    public void setWsHeight(String wsHeight) {
        this.wsHeight = wsHeight;
    }
}
