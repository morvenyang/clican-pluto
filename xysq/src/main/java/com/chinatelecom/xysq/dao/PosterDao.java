package com.chinatelecom.xysq.dao;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.Poster;

public interface PosterDao {

	public Poster findPosterById(Long id);

	public PageList<Poster> findPoster(int page, int pageSize);

}
