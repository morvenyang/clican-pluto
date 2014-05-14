package com.peacebird.dataserver.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.peacebird.dataserver.bean.BrandResult;
import com.peacebird.dataserver.dao.DataDao;

public class DataDaoImpl extends HibernateDaoSupport implements DataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<BrandResult> getIndexResult(Date date, String[] brands) {
		String hsql = "select new com.peacebird.dataserver.bean.BrandResult(brand,sum(dayAmount)) from DayRetailChannel";
		String bs = "";
		for (String brand : brands) {
			bs += "'" + brand + "',";
		}
		if (bs.endsWith(",")) {
			bs = bs.substring(0, bs.length() - 1);
		}
		hsql += " where date = :date and brand in ("+bs+") group by brand";
		return this.getHibernateTemplate().findByNamedParam(hsql,
				new String[] { "date"}, new Object[] { date});
	}

}
