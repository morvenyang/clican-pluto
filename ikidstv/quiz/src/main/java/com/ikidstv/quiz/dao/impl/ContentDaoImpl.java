package com.ikidstv.quiz.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ikidstv.quiz.dao.ContentDao;
import com.ikidstv.quiz.smit.model.Episode;

public class ContentDaoImpl extends HibernateDaoSupport implements ContentDao {

	@SuppressWarnings("unchecked")
	public List<Episode> findAllEpisodes(){
		return this.getHibernateTemplate().find("select e from Episode e join fetch e.season");
	}
}
