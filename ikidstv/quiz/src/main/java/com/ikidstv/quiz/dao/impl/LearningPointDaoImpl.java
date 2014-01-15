package com.ikidstv.quiz.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ikidstv.quiz.dao.LearningPointDao;
import com.ikidstv.quiz.model.LearningPoint;

public class LearningPointDaoImpl extends HibernateDaoSupport implements
		LearningPointDao {

	@SuppressWarnings("unchecked")
	public List<LearningPoint> getAllLearningPoint() {
		return this.getHibernateTemplate().find("from LearningPoint");
	}

}
