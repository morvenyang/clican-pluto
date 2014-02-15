package com.ikidstv.quiz.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ikidstv.quiz.dao.ImageDao;
import com.ikidstv.quiz.model.Image;

public class ImageDaoImpl extends HibernateDaoSupport implements ImageDao {

	@SuppressWarnings("unchecked")
	public List<Image> getImageByContent(String season, String episode) {
		List<Image> result = this.getHibernateTemplate().findByNamedParam(
				"from Image where season = :season and episode = :episode",
				new String[] { "season", "episode" },
				new String[] { season, episode });
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Image> getImageByContent(String season) {
		List<Image> result = this.getHibernateTemplate().findByNamedParam(
				"from Image where season = :season ", "season", season);
		return result;
	}

	public void saveImage(Image image) {
		this.getHibernateTemplate().save(image);
	}

}
