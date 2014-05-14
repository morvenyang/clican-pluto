package com.peacebird.dataserver.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.peacebird.dataserver.bean.IndexBrandResult;
import com.peacebird.dataserver.dao.DataDao;

public class DataDaoImpl extends HibernateDaoSupport implements DataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<IndexBrandResult> getIndexResult(Date date, String[] brands) {
		String hsql = "select new com.peacebird.dataserver.bean.IndexBrandResult(brand,sum(dayAmount)) from DayRetailChannel";
		hsql += " where date = :date and brand in (:brands) group by brand";
		String bs = "";
		for (String brand : brands) {
			bs += "'" + brand + "',";
		}
		if (bs.endsWith(",")) {
			bs = bs.substring(0, bs.length() - 1);
		}
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date", "brands" }, new Object[] { date, bs });
	}

}
