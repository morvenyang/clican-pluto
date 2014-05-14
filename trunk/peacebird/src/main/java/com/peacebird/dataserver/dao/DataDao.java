package com.peacebird.dataserver.dao;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.bean.BrandResult;

public interface DataDao {

	public List<BrandResult> getBrandResult(Date date,String[] brands);
	
	
	public BrandResult getBrandResult(Date date,String brand);
	
	public List<BrandResult> getBrandResultByChannel(Date date, String brand);
	
	
	public List<BrandResult> getBrandWeekResult(Date startDate,Date endDate,String brand);

}
