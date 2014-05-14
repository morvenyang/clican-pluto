package com.peacebird.dataserver.dao;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.bean.IndexResult;

public interface DataDao {

	public List<IndexResult> getIndexResult(Date date);

}
