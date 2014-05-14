package com.peacebird.dataserver.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.peacebird.dataserver.bean.IndexResult;
import com.peacebird.dataserver.dao.DataDao;

public class DataDaoImpl extends HibernateDaoSupport implements DataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<IndexResult> getIndexResult(Date date) {
		String hsql = "select new com.peacebird.dataserver.bean.IndexResult(brand,sum(dayAmount)) from DayRetailChannel";
		hsql += " where date = :date group by brand";
		return this.getHibernateTemplate().findByNamedParam(hsql, "date", date);
	}

}
