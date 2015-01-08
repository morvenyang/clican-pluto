package com.chinatelecom.xysq.service.impl;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.PosterDao;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.PosterService;

public class PosterServiceImpl implements PosterService {
	
	private PosterDao posterDao;

	public void setPosterDao(PosterDao posterDao) {
		this.posterDao = posterDao;
	}

	@Override
	public PageList<Poster> findPosterByOwner(User owner, int page, int pageSize) {
		return posterDao.findPosterByOwner(owner, page, pageSize);
	}

	@Override
	public Poster findPosterById(Long id) {
		return posterDao.findPosterById(id);
	}

	@Override
	public void savePoster(Poster poster) {
		posterDao.savePoster(poster);
	}

	@Override
	public void deletePoster(Poster poster) {
		posterDao.deletePoster(poster);
	}
	
	

}
