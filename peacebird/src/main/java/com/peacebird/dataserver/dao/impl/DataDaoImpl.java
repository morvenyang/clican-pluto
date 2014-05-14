package com.peacebird.dataserver.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.peacebird.dataserver.bean.IndexAmountResult;
import com.peacebird.dataserver.dao.DataDao;

public class DataDaoImpl extends HibernateDaoSupport implements DataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<IndexAmountResult> getIndexResult(Date date) {
		String hsql = "select new com.peacebird.dataserver.bean.IndexAmountResult(brand,sum(dayAmount)) from DayRetailChannel";
		hsql += " where date = :date group by brand";
		return this.getHibernateTemplate().findByNamedParam(hsql, "date", date);
	}

}
