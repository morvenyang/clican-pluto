package com.chinatelecom.xysq.dao.impl;

import java.util.List;

import com.chinatelecom.xysq.dao.AwardDao;
import com.chinatelecom.xysq.model.Award;

public class AwardDaoImpl extends BaseDao implements AwardDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Award> findAllAwards() {
		return (List<Award>)this.getHibernateTemplate().find("from Award");
	}

	@Override
	public void deleteAward(Award award) {
		this.getHibernateTemplate().delete(award);
	}

	@Override
	public void saveAward(Award award) {
		this.getHibernateTemplate().saveOrUpdate(award);
	}

}
