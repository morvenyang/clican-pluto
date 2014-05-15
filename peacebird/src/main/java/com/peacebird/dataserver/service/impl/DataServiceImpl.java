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
import com.peacebird.dataserver.bean.ChannelResult;
import com.peacebird.dataserver.bean.ChannelStatResult;
import com.peacebird.dataserver.bean.IndexStatResult;
import com.peacebird.dataserver.bean.RetailResult;
import com.peacebird.dataserver.bean.RetailStatResult;
import com.peacebird.dataserver.dao.DataDao;
import com.peacebird.dataserver.service.DataService;
import com.peacebird.dataserver.util.DateJsonValueProcessor;

public class DataServiceImpl implements DataService {

	private DataDao dataDao;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	@Override
	public String getIndexResult(String[] brands) {
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
		IndexStatResult ir = new IndexStatResult();
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
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
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
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		String result = JSONObject.fromObject(bsr, jsonConfig).toString();
		return result;
	}

	@Override
	public String getRetailResult(String brand) {
		RetailStatResult rsr = new RetailStatResult();
		Date yesterday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		yesterday = DateUtils.addDays(yesterday, -1);
		List<RetailResult> channelRetail = this.dataDao.getRetailChannelResult(
				yesterday, brand);
		List<RetailResult> sortRetail = this.dataDao.getRetailSortResult(
				yesterday, brand);
		List<RetailResult> regionRetail = this.dataDao.getRetailRegionResult(
				yesterday, brand);
		rsr.setResult(0);
		rsr.setChannelRetail(channelRetail);
		rsr.setSortRetail(sortRetail);
		rsr.setRegionRetail(regionRetail);

		String result = JSONObject.fromObject(rsr).toString();
		return result;
	}

	@Override
	public String getChannelResult(String brand) {
		RetailStatResult rsr = new RetailStatResult();
		Date yesterday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		yesterday = DateUtils.addDays(yesterday, -1);
		List<ChannelResult> channelResult = this.dataDao.getChannelResult(
				yesterday, brand);
		ChannelStatResult csr = new ChannelStatResult();
		csr.setChannel(channelResult);
		csr.setResult(1);

		String result = JSONObject.fromObject(rsr).toString();
		return result;
	}

}
