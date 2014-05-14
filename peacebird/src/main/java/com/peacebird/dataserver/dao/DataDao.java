package com.peacebird.dataserver.dao;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.bean.IndexBrandResult;

public interface DataDao {

	public List<IndexBrandResult> getIndexResult(Date date);

}
