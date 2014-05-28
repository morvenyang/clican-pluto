package com.peacebird.dataserver.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.peacebird.dataserver.bean.BrandResult;
import com.peacebird.dataserver.bean.ChannelResult;
import com.peacebird.dataserver.bean.RankResult;
import com.peacebird.dataserver.bean.RetailResult;
import com.peacebird.dataserver.dao.DataDao;
import com.peacebird.dataserver.model.DayStatus;
import com.peacebird.dataserver.model.DimBrand;

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
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date", "brand" }, new Object[] { date, brand });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BrandResult> getBrandWeekResult(Date startDate, Date endDate,
			String brand) {
		String hsql = "select new com.peacebird.dataserver.bean.BrandResult('',date,sum(dayAmount)) from DayRetailChannel";
		hsql += " where brand = :brand and date>= :startDate and date<= :endDate group by date order by date";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "brand", "startDate", "endDate" },
				new Object[] { brand, startDate, endDate });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RetailResult> getRetailChannelResult(Date date, String brand) {
		String hsql = "select new com.peacebird.dataserver.bean.RetailResult('channel',channel,sum(dayAmount)) from DayRetailChannel";
		hsql += " where brand = :brand and date = :date group by channel";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date", "brand" }, new Object[] { date, brand });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RetailResult> getRetailSortResult(Date date, String brand) {
		String hsql = "select new com.peacebird.dataserver.bean.RetailResult('sort',sort,sum(dayAmount)) from DayRetailSort";
		hsql += " where brand = :brand and date = :date group by sort";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date", "brand" }, new Object[] { date, brand });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RetailResult> getRetailRegionResult(Date date, String brand) {
		String hsql = "select new com.peacebird.dataserver.bean.RetailResult('region',region,sum(dayAmount)) from DayRetailRegion";
		hsql += " where brand = :brand and date = :date group by region";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date", "brand" }, new Object[] { date, brand });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChannelResult> getChannelResult(Date date, String brand) {
		String hsql = "select new com.peacebird.dataserver.bean.ChannelResult(c.dayAmount,cd.docNum,cd.avgDocCount,cd.avgPrice,cd.aps,cd.channel) from DayRetailChannelDetail cd,DayRetailChannel c";
		hsql += " where cd.brand = :brand and cd.date = :date and cd.channel=c.channel and cd.brand=c.brand and cd.date=c.date";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date", "brand" }, new Object[] { date, brand });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllChannelForRank(Date date, String brand) {
		String hsql = "select distinct channel from DayStoreAmountRank where date = :date and brand= :brand";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date", "brand" }, new Object[] { date, brand });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RankResult> getRankResult(final Date date, final String brand,
			final String channel) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hsql = "select new com.peacebird.dataserver.bean.RankResult(name,amount) from DayStoreAmountRank where date = :date and brand= :brand and channel = :channel order by rank";
				Query query = session.createQuery(hsql);
				query.setParameter("date", date);
				query.setParameter("brand", brand);
				query.setParameter("channel", channel);
				query.setMaxResults(10);
				return query.list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RankResult> getAllRankResult(final Date date, final String brand) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hsql = "select new com.peacebird.dataserver.bean.RankResult(name,amount) from DayStoreAmountRank where date = :date and brand= :brand order by amount desc";
				Query query = session.createQuery(hsql);
				query.setParameter("date", date);
				query.setParameter("brand", brand);
				query.setMaxResults(10);
				return query.list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public DayStatus getDayStatus(Date date) {
		List<DayStatus> result = this.getHibernateTemplate().findByNamedParam(
				"from DayStatus where date = :date", "date", date);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public void saveDayStatus(DayStatus dayStatus) {
		this.getHibernateTemplate().saveOrUpdate(dayStatus);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DimBrand> getAllBrands() {
		return this.getHibernateTemplate().find("from DimBrand");
	}

}
