/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.comparator;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.common.util.BeanUtils;

public class CreateTimeComparator<T> extends BaseComparator<T> implements
		Comparator<T> {

	private final static Log log = LogFactory
			.getLog(CreateTimeComparator.class);

	public int compare(T o1, T o2) {
		try {
			return convertByOrder(BeanUtils.getCalendarProperty(o1,
					"createTime").compareTo(
					BeanUtils.getCalendarProperty(o2, "createTime")));
		} catch (Exception e) {
			log.error("", e);
		}
		return 0;
	}

}

// $Id$