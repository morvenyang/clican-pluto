package com.ikidstv.quiz.service;

import java.util.List;

import com.ikidstv.quiz.model.Image;

public interface ImageService {

	public List<Image> getImageByContent(String season, String episode);

	public List<Image> getImageByContent(String season);

	public void saveImage(Image image);
}
