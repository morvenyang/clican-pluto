package com.ikidstv.quiz.dao;

import java.util.List;

import com.ikidstv.quiz.smit.model.Episode;

public interface ContentDao {

	public List<Episode> findAllEpisodes();
}
