package com.peacebird.dataserver.dao;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.bean.BrandResult;
import com.peacebird.dataserver.bean.ChannelResult;
import com.peacebird.dataserver.bean.RankResult;
import com.peacebird.dataserver.bean.RetailResult;
import com.peacebird.dataserver.model.DayStatus;
import com.peacebird.dataserver.model.DimBrand;

public interface DataDao {

	public List<BrandResult> getBrandResult(Date date, String[] brands);

	public BrandResult getBrandResult(Date date, String brand);

	public List<BrandResult> getBrandResultByChannel(Date date, String brand);

	public List<BrandResult> getBrandWeekResult(Date startDate, Date endDate,
			String brand);

	public List<RetailResult> getRetailChannelResult(Date date, String brand);

	public List<RetailResult> getRetailSortResult(Date date, String brand);

	public List<RetailResult> getRetailRegionResult(Date date, String brand);

	public List<ChannelResult> getChannelResult(Date date, String brand);

	public List<String> getAllChannelForRank(Date date, String brand);

	public List<RankResult> getRankResult(Date date, String brand,
			String channel);

	public DayStatus getDayStatus(Date date);

	public void saveDayStatus(DayStatus dayStatus);

	public List<DimBrand> getAllBrands();
}
