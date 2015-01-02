package com.chinatelecom.xysq.bean;

import java.util.ArrayList;
import java.util.List;

public class PageList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5939492368838492671L;
	private int maxCount;
	private int page;
	private int pageSize;

	public PageList(List<T> list, int page, int pageSize, int maxCount,
			T mockObject) {
		this.page = page;
		this.pageSize = pageSize;
		this.maxCount = maxCount;
		int j = 0;
		for (int i = 0; i < maxCount; i++) {
			if (i >= (page - 1) * pageSize && i < page * pageSize
					&& j < list.size()) {
				this.add(list.get(j));
			} else {
				this.add(mockObject);
			}
		}
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
