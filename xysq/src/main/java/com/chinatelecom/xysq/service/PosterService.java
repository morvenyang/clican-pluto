package com.chinatelecom.xysq.service;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.User;

public interface PosterService {

	public PageList<Poster> findPosterByOwner(User owner, int page, int pageSize);

	public Poster findPosterById(Long id);
	
	public void savePoster(Poster poster);
	
	public void deletePoster(Poster poster);

}
