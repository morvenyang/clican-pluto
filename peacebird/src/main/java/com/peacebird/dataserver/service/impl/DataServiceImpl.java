package com.peacebird.dataserver.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.peacebird.dataserver.bean.BrandResult;
import com.peacebird.dataserver.bean.BrandStatResult;
import com.peacebird.dataserver.bean.ChannelRankResult;
import com.peacebird.dataserver.bean.ChannelResult;
import com.peacebird.dataserver.bean.ChannelStatResult;
import com.peacebird.dataserver.bean.Constants;
import com.peacebird.dataserver.bean.GoodRankResult;
import com.peacebird.dataserver.bean.GoodRankStatResult;
import com.peacebird.dataserver.bean.IndexStatResult;
import com.peacebird.dataserver.bean.RetailChartResult;
import com.peacebird.dataserver.bean.RetailResult;
import com.peacebird.dataserver.bean.SpringProperty;
import com.peacebird.dataserver.bean.StoreRankResult;
import com.peacebird.dataserver.bean.StoreRankStatResult;
import com.peacebird.dataserver.bean.comp.ChannelComparator;
import com.peacebird.dataserver.dao.DataDao;
import com.peacebird.dataserver.model.DayStatus;
import com.peacebird.dataserver.model.DimBrand;
import com.peacebird.dataserver.service.DataService;
import com.peacebird.dataserver.util.DateJsonValueProcessor;
import com.peacebird.dataserver.util.IntegerJsonValueProcessor;

public class DataServiceImpl implements DataService {

	private final static Log log = LogFactory.getLog(DataServiceImpl.class);

	private DataDao dataDao;

	private SpringProperty springProperty;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
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
				yesterday = this.dataDao.getPreviousDate();
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
			DayStatus ds = dataDao.getDayStatus(date);
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
		ir.setDate(yesterday);
		Date realYesterday = DateUtils.addDays(
				DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), -1);
		ir.setYesterday(realYesterday.equals(yesterday));
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(ir, jsonConfig).toString();
		return result;
	}

	@Override
	public String getBrandResultForApple(String brand, Date date) {
		Date yesterday = getYesterday(date);

		BrandResult br = this.dataDao.getBrandResult(yesterday, brand);
		BrandStatResult bsr = new BrandStatResult();
		if (br == null) {
			bsr.setMessage("当前该品牌没有昨日的数据");
			return JSONObject.fromObject(bsr).toString();
		}
		br.setBrand(brand);
		br.setDate(yesterday);
		Calendar cal = Calendar.getInstance();
		cal.setTime(yesterday);
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		Date startDate = cal.getTime();
		Date endDate = DateUtils.addDays(startDate, 6);
		List<BrandResult> bwr = this.dataDao.getBrandWeekResult(startDate,
				endDate, brand);
		List<BrandResult> finalBwr = new ArrayList<BrandResult>();
		Map<Long, BrandResult> bMap = new HashMap<Long, BrandResult>();
		for (BrandResult b : bwr) {
			bMap.put(b.getDate().getTime(), b);
		}
		Date d = startDate;
		while (d.compareTo(endDate) <= 0) {
			BrandResult r = bMap.get(d.getTime());
			if (r == null) {
				r = new BrandResult("", d, null);
			}
			finalBwr.add(r);
			d = DateUtils.addDays(d, 1);
		}

		List<BrandResult> bcr = this.dataDao.getBrandResultByChannel(yesterday,
				brand);
		Double lastDayAmount = 0.0;
		Double lastWeekAmount = 0.0;
		Double lastMonthAmount = 0.0;
		Double lastYearAmount = 0.0;
		for (BrandResult b : bcr) {
			if (b.getPerDayAmount() != null) {
				lastDayAmount += b.getPerDayAmount();
			}
			if (b.getPerWeekAmount() != null) {
				lastWeekAmount += b.getPerWeekAmount();
			}
			if (b.getPerMonthAmount() != null) {
				lastMonthAmount += b.getPerMonthAmount();
			}
			if (b.getPerYearAmount() != null) {
				lastYearAmount += b.getPerYearAmount();
			}
		}
		if (br.getDayAmount() != null && lastDayAmount != 0) {
			br.setDayLike(br.getDayAmount() / lastDayAmount);
		}
		if (br.getWeekAmount() != null && lastWeekAmount != 0) {
			br.setWeekLike(br.getDayAmount() / lastWeekAmount);
		}
		if (br.getMonthAmount() != null && lastMonthAmount != 0) {
			br.setMonthLike(br.getDayAmount() / lastMonthAmount);
		}
		if (br.getYearAmount() != null && lastYearAmount != 0) {
			br.setYearLike(br.getDayAmount() / lastYearAmount);
		}

		Collections.sort(bcr);
		bsr.setBrand(brand);
		bsr.setResult(0);
		bsr.setBrandResult(br);
		bsr.setChannels(bcr);
		for (BrandResult fbwr : finalBwr) {
			fbwr.setDayAmount(null);
		}
		bsr.setWeeks(finalBwr);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(bsr, jsonConfig).toString();
		return result;
	}

	@Override
	public String getBrandResult(String brand, Date date) {
		Date yesterday = getYesterday(date);

		BrandResult br = this.dataDao.getBrandResult(yesterday, brand);
		BrandStatResult bsr = new BrandStatResult();
		if (br == null) {
			bsr.setMessage("当前该品牌没有昨日的数据");
			return JSONObject.fromObject(bsr).toString();
		}
		br.setBrand(brand);
		br.setDate(yesterday);
		Calendar cal = Calendar.getInstance();
		cal.setTime(yesterday);
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		Date startDate = cal.getTime();
		Date endDate = DateUtils.addDays(startDate, 6);
		List<BrandResult> bwr = this.dataDao.getBrandWeekResult(startDate,
				endDate, brand);
		List<BrandResult> finalBwr = new ArrayList<BrandResult>();
		Map<Long, BrandResult> bMap = new HashMap<Long, BrandResult>();
		for (BrandResult b : bwr) {
			bMap.put(b.getDate().getTime(), b);
		}
		Date d = startDate;
		while (d.compareTo(endDate) <= 0) {
			BrandResult r = bMap.get(d.getTime());
			if (r == null) {
				r = new BrandResult("", d, null);
			}
			finalBwr.add(r);
			d = DateUtils.addDays(d, 1);
		}

		List<BrandResult> bcr = this.dataDao.getBrandResultByChannel(yesterday,
				brand);
		Double lastDayAmount = 0.0;
		Double dayAmount = 0.0;
		Double lastWeekAmount = 0.0;
		Double weekAmount = 0.0;
		Double lastMonthAmount = 0.0;
		Double monthAmount = 0.0;
		Double lastYearAmount = 0.0;
		Double yearAmount = 0.0;
		for (BrandResult b : bcr) {
			if (b.getChannel().equals(Constants.B2C)) {
				// 统计数据不包括电商的
				continue;
			}
			if (b.getPerDayAmount() != null) {
				lastDayAmount += b.getPerDayAmount();
			}
			if (b.getDayAmount() != null) {
				dayAmount += b.getDayAmount();
			}
			if (b.getPerWeekAmount() != null) {
				lastWeekAmount += b.getPerWeekAmount();
			}
			if (b.getWeekAmount() != null) {
				weekAmount += b.getWeekAmount();
			}
			if (b.getPerMonthAmount() != null) {
				lastMonthAmount += b.getPerMonthAmount();
			}
			if (b.getMonthAmount() != null) {
				monthAmount += b.getMonthAmount();
			}
			if (b.getPerYearAmount() != null) {
				lastYearAmount += b.getPerYearAmount();
			}
			if (b.getYearAmount() != null) {
				yearAmount += b.getYearAmount();
			}
		}
		if (br.getDayAmount() != null && lastDayAmount != 0) {
			br.setDayLike(dayAmount / lastDayAmount - 1);
		}
		if (br.getWeekAmount() != null && lastWeekAmount != 0) {
			br.setWeekLike(weekAmount / lastWeekAmount - 1);
		}
		if (br.getMonthAmount() != null && lastMonthAmount != 0) {
			br.setMonthLike(monthAmount / lastMonthAmount - 1);
		}
		if (br.getYearAmount() != null && lastYearAmount != 0) {
			br.setYearLike(yearAmount / lastYearAmount - 1);
		}

		Collections.sort(bcr);
		bsr.setBrand(brand);
		bsr.setResult(0);
		bsr.setBrandResult(br);
		bsr.setChannels(bcr);
		bsr.setWeeks(finalBwr);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(Integer.class,
				new IntegerJsonValueProcessor());
		String result = JSONObject.fromObject(bsr, jsonConfig).toString();
		return result;
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
			list = this.dataDao.getRetailChannelResult(yesterday, brand);
		} else if (type.equals("sort")) {
			list = this.dataDao.getRetailSortResult(yesterday, brand);
		} else if (type.equals("region")) {
			list = this.dataDao.getRetailRegionResult(yesterday, brand);
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
		List<ChannelResult> channelResult = this.dataDao.getChannelResult(
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
		List<String> channels = this.dataDao.getAllChannelForRank(yesterday,
				brand);
		Collections.sort(channels, new ChannelComparator());
		List<ChannelRankResult> crrList = new ArrayList<ChannelRankResult>();

		List<StoreRankResult> allRankResult = this.dataDao
				.getAllStoreRankResult(yesterday, brand);
		ChannelRankResult acrr = new ChannelRankResult();
		acrr.setChannel("全部");
		acrr.setRanks(allRankResult);
		crrList.add(0, acrr);

		for (String channel : channels) {
			List<StoreRankResult> rankResult = this.dataDao.getStoreRankResult(
					yesterday, brand, channel);
			ChannelRankResult crr = new ChannelRankResult();
			crr.setChannel(channel);
			crr.setRanks(rankResult);
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
		List<GoodRankResult> rankResult = this.dataDao.getGoodRankResult(
				yesterday, brand);
		try {
			for (GoodRankResult grr : rankResult) {
				grr.setImageLink(springProperty.getServerUrl()
						+ "/peacebird/goodImage.do?path="
						+ URLEncoder.encode(grr.getImageLink(), "utf-8"));
				grr.setImageLinkMin(springProperty.getServerUrl()
						+ "/peacebird/goodImage.do?path="
						+ URLEncoder.encode(grr.getImageLinkMin(), "utf-8"));
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
	public List<DimBrand> getAllBrands() {
		return this.dataDao.getAllBrands();
	}

}
