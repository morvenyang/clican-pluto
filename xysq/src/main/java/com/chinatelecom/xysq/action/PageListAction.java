package com.chinatelecom.xysq.action;

import java.util.List;

import com.chinatelecom.xysq.bean.PageListDataModel;

/**
 * TODO 带分页功能的基类
 * 
 */
public abstract class PageListAction<T> extends BaseAction {
	
	public final static int PAGE_SIZE = 10;
    
    protected int page = 1;

    protected List<Integer> scrollerPageList;

    /**
     * 默认数据模型，如果你有多个数据集需要分页， 请自定义PagedListDataModel和相应的getDataModel方法
     */
    protected PageListDataModel<T> defaultDataModel;

    public int getPage() {
		return getDefaultDataModel().getRowIndex()/PAGE_SIZE+1;
	}

	public void setPage(int page) {
		getDefaultDataModel().setRowIndex((page-1)*PAGE_SIZE);
	}
	
	public int getPageSize(){
		return PAGE_SIZE;
	}
    public abstract PageListDataModel<T> getDefaultDataModel();

}