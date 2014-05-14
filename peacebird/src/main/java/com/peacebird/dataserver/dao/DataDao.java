package com.peacebird.dataserver.dao;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.bean.BrandResult;

public interface DataDao {

	public List<BrandResult> getIndexResult(Date date,String[] brands);

}
