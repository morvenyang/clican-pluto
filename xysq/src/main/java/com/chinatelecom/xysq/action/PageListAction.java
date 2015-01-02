package com.chinatelecom.xysq.action;

import java.util.List;

import com.chinatelecom.xysq.bean.PageListDataModel;

/**
 * TODO 带分页功能的基类
 * 
 */
public abstract class PageListAction<T> extends BaseAction {
    /**
     * 当前页码，跟dataSroller的page属性绑定
     */
    protected int scrollerPage = 1;

    protected int pages = 0;

    protected List<Integer> scrollerPageList;

    /**
     * 默认数据模型，如果你有多个数据集需要分页， 请自定义PagedListDataModel和相应的getDataModel方法
     */
    protected PageListDataModel<T> defaultDataModel;

   

    public int getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(int scrollerPage) {
        this.scrollerPage = scrollerPage;
    }

    public abstract PageListDataModel<T> getDefaultDataModel();

}