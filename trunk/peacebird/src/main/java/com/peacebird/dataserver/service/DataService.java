package com.peacebird.dataserver.service;

import java.util.List;

import com.peacebird.dataserver.model.DimBrand;


public interface DataService {

	public String getIndexResult(String[] brands);
	
	public String getBrandResult(String brand);
	
	public String getRetailResult(String brand);
	
	public String getChannelResult(String brand);
	
	public String getRankResult(String brand);
	
	public List<DimBrand> getAllBrands();
}
