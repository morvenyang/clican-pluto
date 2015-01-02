package com.chinatelecom.xysq.bean;

import java.util.ArrayList;

public class EmptyPageList<T> extends PageList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 239591503988293181L;

	public EmptyPageList(int page, int pageSize) {
		super(new ArrayList<T>(), page, pageSize, 0, null);
	}

}
