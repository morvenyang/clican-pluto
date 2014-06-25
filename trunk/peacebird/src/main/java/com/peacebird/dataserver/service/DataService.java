package com.peacebird.dataserver.service;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.bean.RetailResult;
import com.peacebird.dataserver.model.DimBrand;


public interface DataService {

	public String getIndexResult(String[] brands,Date date);
	
	public String getBrandResult(String brand,Date date);
	
	public String getBrandResultForApple(String brand,Date date);
	
	public String getRetailResult(String brand,Date date);
	
	public List<RetailResult> getRetailChartResult(String brand,String type,Date date);
	
	public String getChannelResult(String brand,Date date);
	
	public String getStoreRankResult(String brand,Date date);
	
	public List<DimBrand> getAllBrands();
}
