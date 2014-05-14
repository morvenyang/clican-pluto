package com.peacebird.dataserver.dao;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.bean.IndexAmountResult;

public interface DataDao {

	public List<IndexAmountResult> getIndexResult(Date date);

}
