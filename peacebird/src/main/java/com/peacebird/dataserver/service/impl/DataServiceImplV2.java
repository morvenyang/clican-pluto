package com.peacebird.dataserver.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.peacebird.dataserver.bean.BrandLineChartResult;
import com.peacebird.dataserver.bean.BrandResult;
import com.peacebird.dataserver.bean.BrandStatResult;
import com.peacebird.dataserver.bean.ChannelRankResult;
import com.peacebird.dataserver.bean.ChannelResult;
import com.peacebird.dataserver.bean.ChannelStatResult;
import com.peacebird.dataserver.bean.Constants;
import com.peacebird.dataserver.bean.DataRetailStoreSumResult;
import com.peacebird.dataserver.bean.DataRetailsNoRetailResult;
import com.peacebird.dataserver.bean.GoodRankResult;
import com.peacebird.dataserver.bean.GoodRankStatResult;
import com.peacebird.dataserver.bean.IndexStatResult;
import com.peacebird.dataserver.bean.RetailChartResult;
import com.peacebird.dataserver.bean.RetailResult;
import com.peacebird.dataserver.bean.SpringProperty;
import com.peacebird.dataserver.bean.StoreRankResult;
import com.peacebird.dataserver.bean.StoreRankStatResult;
import com.peacebird.dataserver.bean.comp.ChannelComparator;
import com.peacebird.dataserver.dao.DataDaoV2;
import com.peacebird.dataserver.model.DataRetailStoreSum;
import com.peacebird.dataserver.model.DataRetailsNoRetail;
import com.peacebird.dataserver.model.DayStatus;
import com.peacebird.dataserver.model.DimBrand;
import com.peacebird.dataserver.service.DataServiceV2;
import com.peacebird.dataserver.util.DateJsonValueProcessor;
import com.peacebird.dataserver.util.IntegerJsonValueProcessor;

public class DataServiceImplV2 implements DataServiceV2 {


	private final static Log log = LogFactory.getLog(DataServiceImpl.class);

	private DataDaoV2 dataDaoV2;

	private SpringProperty springProperty;

	public void setDataDaoV2(DataDaoV2 dataDaoV2) {
		this.dataDaoV2 = dataDaoV2;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	private Date getYesterday(Date date) {
		if (date != null) {
			return date;
		}
		Date yesterday = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.isNotEmpty(springProperty.getYesterday())) {
				yesterday = sdf.parse(springProperty.getYesterday());
			} else {
				yesterday = this.dataDaoV2.getPreviousDate();
				if (yesterday == null) {
					yesterday = DateUtils.addDays(DateUtils.truncate(
							new Date(), Calendar.DAY_OF_MONTH), -1);
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return yesterday;
	}

	private String getErrorResult(int code, String message) {
		return "{\"result\":" + code + ",\"message\":\"" + message + "\"}";
	}

	@Override
	public String getIndexResult(String[] brands, Date date) {
		if (date != null) {
			DayStatus ds = dataDaoV2.getDayStatus(date);
			if (ds == null || ds.getStatus() == null || ds.getStatus() == 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
				return this.getErrorResult(3002, sdf.format(date)
						+ "没有相关数据,请重新选择日期");
			}
		}
		Date yesterday = getYesterday(date);
		Set<String> bSet = new HashSet<String>();
		for (String brand : brands) {
			bSet.add(brand);
		}
		List<BrandResult> indexBrandResults = dataDaoV2.getBrandResult(yesterday,
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
		ir.setDate(yesterday);
		Date realYesterday = DateUtils.addDays(
				DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), -1);
		DayStatus ds = dataDaoV2.getDayStatus(realYesterday);
		if (ds != null && ds.getStatus() != null
				&& ds.getStatus().intValue() != 0) {
			ir.setYesterday(true);
		} else {
			ir.setYesterday(false);
		}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(ir, jsonConfig).toString();
		return result;
	}

	@Override
	public String getBrandResult(String brand, Date date) {
		Date yesterday = getYesterday(date);

		BrandStatResult bsr = new BrandStatResult();
		BrandResult br = new BrandResult(brand, "", 0);
		br.setBrand(brand);
		br.setDate(yesterday);
		Double lastDayAmount = 0.0;
		List<BrandResult> bcr = this.dataDaoV2.getBrandResultByChannel(yesterday,
				brand);
		for (BrandResult b : bcr) {
			if (b.getChannel().equals(Constants.B2C)) {
				// 统计数据不包括电商的
				continue;
			}
			if (b.getPerDayAmount() != null) {
				lastDayAmount+=b.getPerDayAmount();
			}
			if(b.getDayAmount()!=null){
				br.setDayAmount(b.getDayAmount() + br.getDayAmount());
			}
		}
		if(lastDayAmount!=0){
			br.setDayLike(br.getDayAmount() / lastDayAmount - 1);
		}
		Collections.sort(bcr);
		bsr.setBrand(brand);
		bsr.setResult(0);
		bsr.setBrandResult(br);
		bsr.setChannels(bcr);

		Date firstDayOfThisWeek = getCalendarDate(yesterday,Calendar.WEEK_OF_MONTH);
		// daily line chart
		List<BrandLineChartResult> dailyLineChart = this.dataDaoV2
				.getBrandLineChartDayResult(yesterday, brand, "days", 8);
		bsr.setDailyLineChart(dailyLineChart);
		setLineChartColorAndDateStr(dailyLineChart,brand,Calendar.DAY_OF_MONTH,firstDayOfThisWeek);
		List<BrandLineChartResult> weeklyLineChart = this.dataDaoV2
				.getBrandLineChartDayResult(
						this.getCalendarDate(yesterday, Calendar.WEEK_OF_MONTH),
						brand, "weeks", 8);
		bsr.setWeeklyLineChart(weeklyLineChart);
		setLineChartColorAndDateStr(weeklyLineChart,brand,Calendar.WEEK_OF_MONTH,null);
		List<BrandLineChartResult> monthLineChart = this.dataDaoV2
				.getBrandLineChartDayResult(
						this.getCalendarDate(yesterday, Calendar.MONTH), brand,
						"months", 12);
		bsr.setMonthlyLineChart(monthLineChart);
		setLineChartColorAndDateStr(monthLineChart,brand,Calendar.MONTH,null);
		List<BrandLineChartResult> yearLineChart = this.dataDaoV2
				.getBrandLineChartDayResult(
						this.getCalendarDate(yesterday, Calendar.YEAR), brand,
						"years", 3);
		bsr.setYearlyLineChart(yearLineChart);
		setLineChartColorAndDateStr(yearLineChart,brand,Calendar.YEAR,null);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(bsr, jsonConfig).toString();
		return result;
	}

	private void setLineChartColorAndDateStr(List<BrandLineChartResult> lineCharts,String brand,int type,Date firstDayOfThisWeek){
		String color = "#E5006E";
		if(brand.equals("女装")){
			color = "#E5006E";
		}else if(brand.equals("男装")){
			color = "#17387A";
		}else if(brand.equals("乐町")){
			color = "#F08DBA";
		}else if(brand.equals("童装")){
			color = "#F7D800";
		}else if(brand.equals("赫奇")){
			color = "#8BACDB";
		}else if(brand.equals("MG公司")){
			color = "#E51D98";
		}else if(brand.equals("电商")){
			color = "#17387A";
		}
		SimpleDateFormat sdf =null;
		if(type==Calendar.DAY_OF_MONTH){
			sdf = new SimpleDateFormat("MM-dd\nEEEE",Locale.SIMPLIFIED_CHINESE);
		}else if(type==Calendar.WEEK_OF_MONTH){
			sdf = new SimpleDateFormat("MM-dd\n'W'w",Locale.SIMPLIFIED_CHINESE);
		}else if(type==Calendar.MONTH){
			sdf = new SimpleDateFormat("M月",Locale.SIMPLIFIED_CHINESE);
		}else if(type==Calendar.YEAR){
			sdf = new SimpleDateFormat("yyyy",Locale.SIMPLIFIED_CHINESE);
		}
		for(BrandLineChartResult lc:lineCharts){
			lc.setDateStr(sdf.format(lc.getDate()));
			if(type==Calendar.DAY_OF_MONTH&&lc.getDate().compareTo(firstDayOfThisWeek)<0){
				lc.setColor("grey");
			}else{
				lc.setColor(color);
			}
		}
	}
	
	
	private List<RetailResult> filteZero(List<RetailResult> source) {
		List<RetailResult> result = new ArrayList<RetailResult>();
		for (RetailResult rr : source) {
			if (rr.getDayAmount() / 10000 != 0) {
				result.add(rr);
			}
		}
		return result;
	}

	@Override
	public List<RetailResult> getRetailChartResult(String brand, String type,
			Date date) {
		Date yesterday = getYesterday(date);
		List<RetailResult> list = null;
		if (type.equals("channel")) {
			list = this.dataDaoV2.getRetailChannelResult(yesterday, brand);
		} else if (type.equals("sort")) {
			list = this.dataDaoV2.getRetailSortResult(yesterday, brand);
		} else if (type.equals("region")) {
			list = this.dataDaoV2.getRetailRegionResult(yesterday, brand);
		}
		list = filteZero(list);
		Collections.sort(list);
		double total = 0;
		for (RetailResult rr : list) {
			if (rr.getDayAmount() != null) {
				rr.setDayAmount(rr.getDayAmount() / 10000);
			}
			total += rr.getDayAmount();
		}
		if (total != 0) {
			for (RetailResult rr : list) {
				rr.setPercent(rr.getDayAmount() / total);
			}
		}
		return list;
	}

	@Override
	public String getRetailChartResultForJson(String brand, String type,
			Date date) {
		Date yesterday = getYesterday(date);
		List<RetailResult> dataProvider = this.getRetailChartResult(brand,
				type, date);
		RetailChartResult rcr = new RetailChartResult();
		rcr.setDataProvider(dataProvider);
		rcr.setHeight(700 + dataProvider.size() * 80);
		rcr.setTop(300 + dataProvider.size() * 5);

		double total = 0;
		for (RetailResult rr : dataProvider) {
			if (rr.getDayAmount() != null) {
				total += rr.getDayAmount();
			}
		}
		rcr.setDate(yesterday);
		rcr.setTotal(Math.round(total));
		rcr.setCount(dataProvider.size());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());

		String result = JSONObject.fromObject(rcr, jsonConfig).toString();
		return result;
	}

	@Override
	public String getChannelResult(String brand, Date date) {
		Date yesterday = getYesterday(date);
		List<ChannelResult> channelResult = this.dataDaoV2.getChannelResult(
				yesterday, brand);
		Collections.sort(channelResult);
		ChannelStatResult csr = new ChannelStatResult();
		csr.setChannel(channelResult);
		csr.setResult(0);
		csr.setDate(yesterday);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(csr, jsonConfig).toString();
		return result;
	}

	@Override
	public String getStoreRankResult(String brand, Date date) {
		Date yesterday = getYesterday(date);
		List<String> channels = this.dataDaoV2.getAllChannelForRank(yesterday,
				brand);
		Collections.sort(channels, new ChannelComparator());
		List<ChannelRankResult> crrList = new ArrayList<ChannelRankResult>();

		List<StoreRankResult> allRankResult = this.dataDaoV2
				.getAllStoreRankResult(yesterday, brand,"desc");
		List<StoreRankResult> allReverseRankResult = this.dataDaoV2
		.getAllStoreRankResult(yesterday, brand,"asc");
		ChannelRankResult acrr = new ChannelRankResult();
		acrr.setChannel("全部");
		acrr.setRanks(allRankResult);
		acrr.setReverseRanks(allReverseRankResult);
		crrList.add(0, acrr);

		for (String channel : channels) {
			List<StoreRankResult> rankResult = this.dataDaoV2.getStoreRankResult(
					yesterday, brand, channel,"asc");
			List<StoreRankResult> reverseRankResult = this.dataDaoV2.getStoreRankResult(
					yesterday, brand, channel,"desc");
			ChannelRankResult crr = new ChannelRankResult();
			crr.setChannel(channel);
			crr.setRanks(rankResult);
			crr.setReverseRanks(reverseRankResult);
			crrList.add(crr);
		}
		StoreRankStatResult rsr = new StoreRankStatResult();
		rsr.setResult(0);
		rsr.setDate(yesterday);
		rsr.setChannels(crrList);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(rsr, jsonConfig).toString();
		return result;
	}

	@Override
	public String getGoodRankResult(String brand, Date date) {
		Date yesterday = getYesterday(date);
		List<GoodRankResult> rankResult = this.dataDaoV2.getGoodRankResult(
				yesterday, brand);
		try {
			for (GoodRankResult grr : rankResult) {
				if (StringUtils.isNotEmpty(grr.getImageLink())) {
					if (!grr.getImageLink().startsWith("http")) {
						grr.setImageLink(springProperty.getServerUrl()
								+ "/peacebird/goodImage.do?path="
								+ URLEncoder.encode(grr.getImageLink(), "utf-8"));
					}
				} else {
					grr.setImageLink(springProperty.getServerUrl()
							+ "/peacebird/goodImage.do?path="
							+ URLEncoder.encode("nogoods.gif", "utf-8"));

				}
				if (StringUtils.isNotEmpty(grr.getImageLinkMin())) {
					if (!grr.getImageLinkMin().startsWith("http")) {
						grr.setImageLinkMin(springProperty.getServerUrl()
								+ "/peacebird/goodImage.do?path="
								+ URLEncoder.encode(grr.getImageLinkMin(),
										"utf-8"));
					}

				} else {
					grr.setImageLinkMin(springProperty.getServerUrl()
							+ "/peacebird/goodImage.do?path="
							+ URLEncoder.encode("nogoods_min.jpg", "utf-8"));
				}

			}
		} catch (Exception e) {
			log.error("", e);
		}

		GoodRankStatResult grsr = new GoodRankStatResult();
		grsr.setDate(yesterday);
		grsr.setResult(0);
		grsr.setGoods(rankResult);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(grsr, jsonConfig).toString();
		return result;
	}

	@Override
	public String getDataRetailStoreSumResult(String brand, Date date) {
		Date yesterday = getYesterday(date);
		List<DataRetailStoreSum> monthlySums = this.dataDaoV2
				.getDataRetailStoreSum(yesterday, brand, "months");
		
		List<DataRetailStoreSum> yearlySums = this.dataDaoV2
		.getDataRetailStoreSum(yesterday, brand, "years");
		
		Collections.sort(monthlySums);
		Collections.sort(yearlySums);
		
		DataRetailStoreSumResult drssr = new DataRetailStoreSumResult();
		drssr.setDate(yesterday);
		drssr.setResult(0);
		drssr.setMonthlySums(monthlySums);
		drssr.setYearlySums(yearlySums);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(drssr, jsonConfig).toString();
		return result;
	}

	@Override
	public String getDataRetailsNoRetailResult(String brand, Date date) {
		Date yesterday = getYesterday(date);
		List<DataRetailsNoRetail> noRetails = this.dataDaoV2
				.getDataRetailsNoRetail(yesterday, brand);
		
		DataRetailsNoRetailResult drssr = new DataRetailsNoRetailResult();
		drssr.setDate(yesterday);
		drssr.setResult(0);
		drssr.setNoRetails(noRetails);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(drssr, jsonConfig).toString();
		return result;
	}

	private Date getCalendarDate(Date date, int calendar) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (calendar == Calendar.WEEK_OF_MONTH) {
			while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
			}
			return cal.getTime();
		} else if (calendar == Calendar.MONTH) {
			return DateUtils.truncate(cal, Calendar.MONTH).getTime();
		} else if (calendar == Calendar.YEAR) {
			return DateUtils.truncate(cal, Calendar.YEAR).getTime();
		} else {
			return null;
		}
	}

	@Override
	public List<DimBrand> getAllBrands() {
		return this.dataDaoV2.getAllBrands();
	}


}
