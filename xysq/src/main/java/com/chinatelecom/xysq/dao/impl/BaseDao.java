package com.chinatelecom.xysq.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class BaseDao extends HibernateDaoSupport {

	protected <T> List<List<T>> getInIds(List<T> ids) {
		List<List<T>> subIds = new ArrayList<List<T>>();
		for (int i = 0; i < ids.size(); i = i + 1000) {
			if (i + 1000 < ids.size()) {
				subIds.add(ids.subList(i, i + 1000));
			} else {
				subIds.add(ids.subList(i, ids.size()));
			}
		}
		return subIds;
	}
}
