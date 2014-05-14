package com.peacebird.dataserver.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.time.DateUtils;

import com.peacebird.dataserver.bean.BrandResult;
import com.peacebird.dataserver.bean.BrandStatResult;
import com.peacebird.dataserver.bean.IndexResult;
import com.peacebird.dataserver.dao.DataDao;
import com.peacebird.dataserver.service.DataService;
import com.peacebird.dataserver.util.DateJsonValueProcessor;

public class DataServiceImpl implements DataService {

	private DataDao dataDao;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	@Override
	public String getCurrentIndexResult(String[] brands) {
		Date yesterday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		yesterday = DateUtils.addDays(yesterday, -1);
		Set<String> bSet = new HashSet<String>();
		List<BrandResult> indexBrandResults = dataDao.getBrandResult(yesterday,
				brands);
		for (BrandResult ibr : indexBrandResults) {
			bSet.remove(ibr.getBrand());
		}
		for (String brand : bSet) {
			BrandResult ibr = new BrandResult(brand, "", -1);
			indexBrandResults.add(ibr);
		}
		IndexResult ir = new IndexResult();
		ir.setBrands(indexBrandResults);
		ir.setResult(0);
		String result = JSONObject.fromObject(ir).toString();
		return result;
	}

	@Override
	public String getBrandResult(String brand) {
		Date yesterday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		yesterday = DateUtils.addDays(yesterday, -1);

		BrandResult br = this.dataDao.getBrandResult(yesterday, brand);
		Calendar cal = Calendar.getInstance();
		cal.setTime(yesterday);
		while(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.MONDAY){
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		Date startDate = cal.getTime();
		Date endDate = DateUtils.addDays(startDate, 6);
		List<BrandResult> bwr = this.dataDao.getBrandWeekResult(startDate,
				endDate, brand);
		List<BrandResult> bcr = this.dataDao.getBrandResultByChannel(yesterday,
				brand);
		BrandStatResult bsr = new BrandStatResult();
		bsr.setResult(0);
		if (br == null) {
			bsr.setMessage("当前该品牌没有昨日的数据");
		} else {
			bsr.setBrandResult(br);
			bsr.setChannels(bcr);
			bsr.setWeeks(bwr);
		}
		JsonConfig jsonConfig = new JsonConfig(); 
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));  
		String result = JSONObject.fromObject(bsr,jsonConfig).toString();
		return result;
	}
}
