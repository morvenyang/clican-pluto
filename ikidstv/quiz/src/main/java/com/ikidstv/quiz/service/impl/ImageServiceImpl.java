package com.ikidstv.quiz.service.impl;

import java.util.List;

import com.ikidstv.quiz.dao.ImageDao;
import com.ikidstv.quiz.model.Image;
import com.ikidstv.quiz.service.ImageService;

public class ImageServiceImpl implements ImageService {

	private ImageDao imageDao;
	
	public void setImageDao(ImageDao imageDao) {
		this.imageDao = imageDao;
	}

	public List<Image> getImageByContent(String season, String episode) {
		return imageDao.getImageByContent(season, episode);
	}

	public List<Image> getImageByContent(String season) {
		return imageDao.getImageByContent(season);
	}

	public void saveImage(Image image) {
		imageDao.saveImage(image);
	}

}
