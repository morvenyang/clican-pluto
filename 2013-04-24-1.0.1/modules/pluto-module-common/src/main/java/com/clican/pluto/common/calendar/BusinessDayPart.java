/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.calendar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class BusinessDayPart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4026218341236937799L;

	protected int version = 0;
	protected int fromHour = -1;
	protected int fromMinute = -1;
	protected int toHour = -1;
	protected int toMinute = -1;
	protected BusinessDay day = null;
	protected int index = -1;

	public Date add(Date date, long millis, boolean isBusinessTime) {
		Date end = null;

		BusinessCalendar businessCalendar = day.getBusinessCalendar();
		Calendar calendar = businessCalendar.createCalendar();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		long dateMilliseconds = ((hour * 60) + minute) * 60 * 1000;
		long dayPartEndMilleseconds = 1000L * 60 * ((toHour * 60) + toMinute);
		long millisecondsInThisDayPart = dayPartEndMilleseconds
				- dateMilliseconds;

		if (millis <= millisecondsInThisDayPart) {
			end = new Date(date.getTime() + millis);
		} else {
			long remainderMillis = millis - millisecondsInThisDayPart;
			Date dayPartEndDate = new Date(date.getTime() + millis
					- remainderMillis);

			Object[] result = new Object[2];
			day.findNextDayPartStart(index + 1, dayPartEndDate, result);
			Date nextDayPartStart = (Date) result[0];
			BusinessDayPart nextDayPart = (BusinessDayPart) result[1];

			end = nextDayPart.add(nextDayPartStart, remainderMillis,
					isBusinessTime);
		}

		return end;
	}

	public boolean isStartAfter(Date date) {
		Calendar calendar = day.getBusinessCalendar().createCalendar();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		return ((hour < fromHour) || ((hour == fromHour) && (minute <= fromMinute)));
	}

	public boolean includes(Date date) {
		Calendar calendar = day.getBusinessCalendar().createCalendar();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		return (((fromHour < hour) || ((fromHour == hour) && (fromMinute <= minute))) && ((hour < toHour) || ((hour == toHour) && (minute <= toMinute))));
	}

	public Date getStartTime(Date date) {
		Calendar calendar = day.getBusinessCalendar().createCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, fromHour);
		calendar.set(Calendar.MINUTE, fromMinute);
		return calendar.getTime();
	}

	public BusinessDay getDay() {
		return day;
	}

	public void setDay(BusinessDay day) {
		this.day = day;
	}

	public int getFromHour() {
		return fromHour;
	}

	public void setFromHour(int fromHour) {
		this.fromHour = fromHour;
	}

	public int getFromMinute() {
		return fromMinute;
	}

	public void setFromMinute(int fromMinute) {
		this.fromMinute = fromMinute;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getToHour() {
		return toHour;
	}

	public void setToHour(int toHour) {
		this.toHour = toHour;
	}

	public int getToMinute() {
		return toMinute;
	}

	public void setToMinute(int toMinute) {
		this.toMinute = toMinute;
	}

}

// $Id$