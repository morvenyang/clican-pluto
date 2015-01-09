package com.chinatelecom.xysq.dao;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.User;

public interface PosterDao {

	public Poster findPosterById(Long id);

	public PageList<Poster> findPoster(int page, int pageSize);
	
	public PageList<Poster> findPosterByOwner(User owner,int page, int pageSize);
	
	public void savePoster(Poster poster);
	
	public void deletePoster(Poster poster);
	
	public void saveImage(Image image);

}
