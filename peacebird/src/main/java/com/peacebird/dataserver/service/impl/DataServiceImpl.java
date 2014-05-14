package com.peacebird.dataserver.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateUtils;

import com.peacebird.dataserver.bean.IndexBrandResult;
import com.peacebird.dataserver.bean.IndexResult;
import com.peacebird.dataserver.dao.DataDao;
import com.peacebird.dataserver.service.DataService;

public class DataServiceImpl implements DataService {

	private DataDao dataDao;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	@Override
	public String getCurrentIndexResult(String[] brands) {
		Date yesterday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		yesterday = DateUtils.addDays(yesterday, -1);
		List<IndexBrandResult> indexBrandResults = dataDao.getIndexResult(yesterday,brands);
		IndexResult ir = new IndexResult();
		ir.setBrands(indexBrandResults);
		ir.setResult(0);
		String result = JSONObject.fromObject(ir).toString();
		return result;
	}
	
}
