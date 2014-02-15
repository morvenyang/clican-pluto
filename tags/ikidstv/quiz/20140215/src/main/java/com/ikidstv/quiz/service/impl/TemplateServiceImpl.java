package com.ikidstv.quiz.service.impl;

import java.util.List;

import com.ikidstv.quiz.dao.TemplateDao;
import com.ikidstv.quiz.model.Template;
import com.ikidstv.quiz.service.TemplateService;

public class TemplateServiceImpl implements TemplateService {

	private TemplateDao templateDao;

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public List<Template> getAllTemplates() {
		return this.templateDao.getAllTemplates();
	}
	
	
}
