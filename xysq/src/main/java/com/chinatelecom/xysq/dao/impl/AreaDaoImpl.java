package com.chinatelecom.xysq.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.chinatelecom.xysq.model.Area;

public class AreaDaoImpl extends HibernateDaoSupport implements
		com.chinatelecom.xysq.dao.AreaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getAreaTree() {
		return this.getHibernateTemplate().find("from Area where level=1");
	}

}
