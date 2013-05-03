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

public class BusinessDay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6834500498974757729L;

	protected BusinessDayPart[] dayParts = null;
	protected BusinessCalendar businessCalendar = null;

	public void findNextDayPartStart(int dayPartIndex, Date date,
			Object[] result) {
		// if there is a day part in this day that starts after the given date
		if ((dayParts != null) && (dayPartIndex < dayParts.length)) {
			if (dayParts[dayPartIndex].isStartAfter(date)) {
				result[0] = dayParts[dayPartIndex].getStartTime(date);
				result[1] = dayParts[dayPartIndex];
			} else {
				findNextDayPartStart(dayPartIndex + 1, date, result);
			}
		} else {
			// descend recustively
			date = businessCalendar.findStartOfNextDay(date);
			BusinessDay nextDay = businessCalendar.findDay(date);
			nextDay.findNextDayPartStart(0, date, result);
		}
	}

	public BusinessCalendar getBusinessCalendar() {
		return businessCalendar;
	}

	public BusinessDayPart[] getDayParts() {
		return dayParts;
	}

	public void setBusinessCalendar(BusinessCalendar businessCalendar) {
		this.businessCalendar = businessCalendar;
	}

	public void setDayParts(BusinessDayPart[] dayParts) {
		this.dayParts = dayParts;
	}

}

// $Id$