package com.chinatelecom.xysq.service.impl;

import com.chinatelecom.xysq.dao.AreaDao;
import com.chinatelecom.xysq.service.AreaService;

public class AreaServiceImpl implements AreaService {

	private AreaDao areaDao;

	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}
	
	
}
