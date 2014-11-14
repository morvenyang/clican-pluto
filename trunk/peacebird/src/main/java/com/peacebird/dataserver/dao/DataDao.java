package com.peacebird.dataserver.dao;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.bean.BrandLineChartResult;
import com.peacebird.dataserver.bean.BrandResult;
import com.peacebird.dataserver.bean.ChannelResult;
import com.peacebird.dataserver.bean.GoodRankResult;
import com.peacebird.dataserver.bean.RetailResult;
import com.peacebird.dataserver.bean.StoreRankResult;
import com.peacebird.dataserver.model.DayStatus;
import com.peacebird.dataserver.model.DimBrand;

public interface DataDao {

	public Date getPreviousDate();
	
	public List<BrandLineChartResult> getBrandLineChartDayResult(Date date,String brand,String dateType,int days);
	
	public List<BrandResult> getBrandResult(Date date, String[] brands);

	public List<BrandResult> getBrandResultByChannel(Date date, String brand);

	public List<RetailResult> getRetailChannelResult(Date date, String brand);

	public List<RetailResult> getRetailSortResult(Date date, String brand);

	public List<RetailResult> getRetailRegionResult(Date date, String brand);

	public List<ChannelResult> getChannelResult(Date date, String brand);

	public List<String> getAllChannelForRank(Date date, String brand);

	public List<StoreRankResult> getStoreRankResult(Date date, String brand,
			String channel,String order);
	
	public List<StoreRankResult> getAllStoreRankResult(Date date, String brand);
	
	public List<GoodRankResult> getGoodRankResult(Date date, String brand);

	public DayStatus getDayStatus(Date date);

	public void saveDayStatus(DayStatus dayStatus);

	public List<DimBrand> getAllBrands();
}
