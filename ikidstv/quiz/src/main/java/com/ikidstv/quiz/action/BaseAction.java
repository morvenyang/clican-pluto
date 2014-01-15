package com.ikidstv.quiz.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.annotations.intercept.BypassInterceptors;

import com.ikidstv.quiz.bean.MenuNavigation;


public abstract class BaseAction extends SpringBeanAction {

    protected String approvingOpinion;// 审批意见

    protected Map<String, Serializable> taskParamMap = new HashMap<String, Serializable>();

    protected boolean showError = false;
    protected boolean showQueryCom = false;// 显示查询组件
    protected boolean showAddButton = false;// 显示新增按钮
    protected boolean showEditButton = false;// 显示列表编辑按钮
    protected boolean showDeleteButton = false;// 显示列表删除按钮
    protected boolean showDetailButton = false;// 显示列表详情按钮
    protected boolean showExportExcelButton = false;// 显示导出按钮
    protected String selectedTabID;// 被选中的Tab
    protected String showModalOperation = "show";// 弹出框显示/隐藏(show/hide)
    protected byte[] fileUpLoadData;// 文件上传数据
    protected boolean disabled = false;//
    protected MenuNavigation menuNavigation;
    protected String jsVarOfSecurityInfo;
    protected int pageSize;
    protected int rememberPageSize;
    protected String tableName;
    protected Map<String, Integer> tablePageSizeMap = new HashMap<String, Integer>();
    protected String errMsg;

    public MenuNavigation getMenuNavigation(String tabName) {
        if (menuNavigation == null)
            menuNavigation = new MenuNavigation();
        menuNavigation.setTabName(tabName);
        return menuNavigation;
    }

    public void setMenuNavigation(MenuNavigation menuNavigation) {
        this.menuNavigation = menuNavigation;
    }

    

    @BypassInterceptors
    public String getShowModalOperation() {
        return showModalOperation;
    }

    public void setShowModalOperation(String showModalOperation) {
        this.showModalOperation = showModalOperation;
    }

    @BypassInterceptors
    public boolean isShowExportExcelButton() {
        return showExportExcelButton;
    }

    public void setShowExportExcelButton(boolean showExportExcelButton) {
        this.showExportExcelButton = showExportExcelButton;
    }

    @BypassInterceptors
    public boolean isShowError() {
        return showError;
    }

    public void setShowError(boolean isShowError) {
        this.showError = isShowError;
    }

    @BypassInterceptors
    public boolean isShowQueryCom() {
        return showQueryCom;
    }

    public void setShowQueryCom(boolean showQueryCom) {
        this.showQueryCom = showQueryCom;
    }

    @BypassInterceptors
    public boolean isShowAddButton() {
        return showAddButton;
    }

    public void setShowAddButton(boolean showAddButton) {
        this.showAddButton = showAddButton;
    }

    @BypassInterceptors
    public boolean isShowEditButton() {
        return showEditButton;
    }

    public void setShowEditButton(boolean showEditButton) {
        this.showEditButton = showEditButton;
    }

    @BypassInterceptors
    public boolean isShowDeleteButton() {
        return showDeleteButton;
    }

    public void setShowDeleteButton(boolean showDeleteButton) {
        this.showDeleteButton = showDeleteButton;
    }

    @BypassInterceptors
    public boolean isShowDetailButton() {
        return showDetailButton;
    }

    public void setShowDetailButton(boolean showDetailButton) {
        this.showDetailButton = showDetailButton;
    }

    public boolean isDisabled() {
        return disabled;
    }

}
