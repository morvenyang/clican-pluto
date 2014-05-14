package com.peacebird.dataserver.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.peacebird.dataserver.bean.BrandResult;
import com.peacebird.dataserver.dao.DataDao;

public class DataDaoImpl extends HibernateDaoSupport implements DataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<BrandResult> getBrandResult(Date date, String[] brands) {
		String hsql = "select new com.peacebird.dataserver.bean.BrandResult(brand,'',sum(dayAmount)) from DayRetailChannel";
		String bs = "";
		for (String brand : brands) {
			bs += "'" + brand + "',";
		}
		if (bs.endsWith(",")) {
			bs = bs.substring(0, bs.length() - 1);
		}
		hsql += " where date = :date and brand in (" + bs + ") group by brand";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date" }, new Object[] { date });
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrandResult getBrandResult(Date date, String brand) {
		String hsql = "select new com.peacebird.dataserver.bean.BrandResult('',sum(dayAmount),sum(weekAmount),sum(yearAmount),sum(weekLike),sum(yearLike)) from DayRetailChannel";
		hsql += " where date = :date and brand = :brand";
		List<BrandResult> brs = this.getHibernateTemplate().findByNamedParam(
				hsql, new String[] { "date", "brand" },
				new Object[] { date, brand });
		if (brs.size() > 0) {
			return brs.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BrandResult> getBrandResultByChannel(Date date, String brand) {
		String hsql = "select new com.peacebird.dataserver.bean.BrandResult('',channel,sum(dayAmount)) from DayRetailChannel";
		hsql += " where date = :date and brand = :brand group by channel";
		return this.getHibernateTemplate().findByNamedParam(
				hsql, new String[] { "date", "brand" },
				new Object[] { date, brand });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BrandResult> getBrandWeekResult(Date startDate, Date endDate,
			String brand) {
		String hsql = "select new com.peacebird.dataserver.bean.BrandResult('',date,sum(dayAmount)) from DayRetailChannel";
		hsql += " where brand = :brand and date>= :startDate and date<= :endDate group by date";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "brand","startDate","endDate" }, new Object[] { brand,startDate,endDate });
	}
}
