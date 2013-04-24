/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.calendar;

import java.io.Serializable;
import java.util.Date;

public class Holiday implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9077937925052861782L;

	protected Date fromDay = null;
	protected Date toDay = null;

	public boolean includes(Date date) {
		return ((fromDay.getTime() <= date.getTime()) && (date.getTime() < toDay
				.getTime()));
	}

	public Date getFromDay() {
		return fromDay;
	}

	public void setFromDay(Date fromDay) {
		this.fromDay = fromDay;
	}

	public Date getToDay() {
		return toDay;
	}

	public void setToDay(Date toDay) {
		this.toDay = toDay;
	}
}

// $Id$