package com.ikidstv.quiz.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ikidstv.quiz.dao.TemplateDao;
import com.ikidstv.quiz.model.Template;

public class TemplateDaoImpl extends HibernateDaoSupport implements TemplateDao {

	@SuppressWarnings("unchecked")
	public List<Template> getAllTemplates() {
		return this.getHibernateTemplate().find("from Template");
	}

}
