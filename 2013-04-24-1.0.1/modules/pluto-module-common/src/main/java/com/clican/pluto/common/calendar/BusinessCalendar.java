/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.calendar;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * This class<code>BusinessCalendar</code> represents the calendar in work flow 
 * configured in spring configurations.
 * <p>
 * <pre>
 * Example:
 * <bean id="businessCalendar" class="com.jsw.common.calendar.BusinessCalendar" init-method="init">
 *		<property name="monday" value="9:00-12:00 and 13:00-18:00"/>
 *	 	<property name="tuesday" value="9:00-12:00 and 13:00-18:00"/>
 *		<property name="wednesday" value="9:00-12:00 and 13:00-18:00"/>
 *		<property name="thursday" value="9:00-12:00 and 13:00-18:00"/>
 *		<property name="friday" value="9:00-12:00 and 13:00-18:00"/>
 *		<property name="holidayList">
 *			<list>
 *				<value>01/07/2008-31/08/2008</value>
 *				<value>01/10/2008-07/10/2008</value>
 *			</list>
 *		</property>
 *	</bean>
 * </pre>
 * </p>
 * @author chulin.gui
 *
 */
public class BusinessCalendar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8371169448495697670L;

	/**
	 * array that contains the weekdays in the index as specified by
	 * {@link Calendar#SUNDAY} (=1), {@link Calendar#MONDAY} (=2),...
	 * {@link Calendar#SATURDAY} (=7).
	 */
	protected BusinessDay[] days = null;
	protected Holiday[] holidays = null;

	protected long secondInMillis = 1000;
	protected long minuteInMillis = 60000;
	protected long hourInMillis = 3600000;
	protected long dayInMillis = 24 * hourInMillis;
	protected long weekInMillis = 7 * dayInMillis;
	protected long monthInMillis = 30 * dayInMillis;
	protected long yearInMillis = 365 * dayInMillis;

	protected long businessDayInMillis = 8 * hourInMillis;
	protected long businessWeekInMillis = 40 * hourInMillis;
	protected long businessMonthInMillis = 21 * dayInMillis;
	protected long businessYearInMillis = 220 * dayInMillis;

	private String monday;

	private String tuesday;

	private String wednesday;

	private String thursday;

	private String friday;

	private String saturday;

	private String sunday;

	private List<String> holidayList;

	public void setMonday(String monday) {
		this.monday = monday;
	}

	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	public void setFriday(String friday) {
		this.friday = friday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public void setHolidayList(List<String> holidayList) {
		this.holidayList = holidayList;
	}

	/**
	 * Initial method 
	 */
	public void init() {
		days = new BusinessDay[8];
		days[Calendar.SUNDAY] = parseDay(sunday);
		days[Calendar.MONDAY] = parseDay(monday);
		days[Calendar.TUESDAY] = parseDay(tuesday);
		days[Calendar.WEDNESDAY] = parseDay(wednesday);
		days[Calendar.THURSDAY] = parseDay(thursday);
		days[Calendar.FRIDAY] = parseDay(friday);
		days[Calendar.SATURDAY] = parseDay(saturday);

		holidays = new Holiday[holidayList.size()];
		for (int i = 0; i < holidayList.size(); i++) {
			holidays[i] = parseHoliday(holidayList.get(i));
		}
	}

	/**
	 * parse variable days string.
	 * @param dayExpr
	 * @return
	 */
	private BusinessDay parseDay(String dayExpr) {
		DateFormat hourFormat = new SimpleDateFormat("HH:mm");
		BusinessDay day = new BusinessDay();
		day.setBusinessCalendar(this);
		if (StringUtils.isNotEmpty(dayExpr)) {
			List<BusinessDayPart> dayParts = new ArrayList<BusinessDayPart>();
			int dayPartIndex = 0;
			for (String part : com.clican.pluto.common.util.StringUtils.tokenize(
					dayExpr, "and")) {
				try {
					int separatorIndex = part.indexOf('-');
					if (separatorIndex == -1)
						throw new IllegalArgumentException("no dash (-)");
					String fromText = part.substring(0, separatorIndex).trim()
							.toLowerCase();
					String toText = part.substring(separatorIndex + 1).trim()
							.toLowerCase();

					Date from = hourFormat.parse(fromText);
					Date to = hourFormat.parse(toText);

					Calendar calendar = new GregorianCalendar();
					calendar.setTime(from);
					int fromHour = calendar.get(Calendar.HOUR_OF_DAY);
					int fromMinute = calendar.get(Calendar.MINUTE);

					calendar.setTime(to);
					int toHour = calendar.get(Calendar.HOUR_OF_DAY);
					if (toHour == 0) {
						toHour = 24;
					}
					int toMinute = calendar.get(Calendar.MINUTE);

					BusinessDayPart dayPart = new BusinessDayPart();
					dayPart.setDay(day);
					dayPart.setIndex(dayPartIndex);
					dayPart.setFromHour(fromHour);
					dayPart.setFromMinute(fromMinute);
					dayPart.setToHour(toHour);
					dayPart.setToMinute(toMinute);
					dayParts.add(dayPart);
					dayPartIndex++;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			BusinessDayPart[] dayPartArray = new BusinessDayPart[dayParts
					.size()];
			dayPartArray = dayParts.toArray(dayPartArray);
			day.setDayParts(dayPartArray);
		}
		return day;
	}

	/**
	 * parse holiday string.
	 * @param holidayExpr
	 * @return
	 */
	private Holiday parseHoliday(String holidayExpr) {
		DateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
		Holiday holiday = new Holiday();
		try {
			int dashIndex = holidayExpr.indexOf('-');
			String fromDateText = null;
			String toDateText = null;
			if (dashIndex != -1) {
				fromDateText = holidayExpr.substring(0, dashIndex).trim()
						.toLowerCase();
				toDateText = holidayExpr.substring(dashIndex + 1).trim()
						.toLowerCase();
			} else {
				fromDateText = holidayExpr.trim().toLowerCase();
				toDateText = fromDateText;
			}
			Date fromDate = dayFormat.parse(fromDateText);
			holiday.setFromDay(fromDate);
			Date toDate = dayFormat.parse(toDateText);
			holiday.setToDay(toDate);

			// now we are going to set the toDay to the end of the day, rather
			// then the beginning.
			// we take the start of the next day as the end of the toDay.
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(holiday.getToDay());
			calendar.add(Calendar.DATE, 1);
			Date toDay = calendar.getTime();
			holiday.setToDay(toDay);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return holiday;
	}

	public Date add(Date date, String duration) {
		return add(date, new Duration(duration));
	}

	public Date add(Date date, Duration duration) {
		Date end = null;
		if (duration.isBusinessTime()) {
			BusinessDayPart dayPart = findDayPart(date);
			boolean isInbusinessHours = (dayPart != null);
			if (!isInbusinessHours) {
				Object[] result = new Object[2];
				findDay(date).findNextDayPartStart(0, date, result);
				date = (Date) result[0];
				dayPart = (BusinessDayPart) result[1];
			}
			long millis = convertToMillis(duration);
			end = dayPart.add(date, millis, duration.isBusinessTime());
		} else {
			long millis = convertToMillis(duration);
			end = new Date(date.getTime() + millis);
		}
		return end;
	}

	public long convertToMillis(Duration duration) {
		long millis = duration.getMillis();
		millis += duration.getSeconds() * secondInMillis;
		millis += duration.getMinutes() * minuteInMillis;
		millis += duration.getHours() * hourInMillis;
		if (duration.isBusinessTime()) {
			millis += duration.getDays() * businessDayInMillis;
			millis += duration.getWeeks() * businessWeekInMillis;
			millis += duration.getMonths() * businessMonthInMillis;
			millis += duration.getYears() * businessYearInMillis;
		} else {
			millis += duration.getDays() * dayInMillis;
			millis += duration.getWeeks() * weekInMillis;
			millis += duration.getMonths() * monthInMillis;
			millis += duration.getYears() * yearInMillis;
		}
		return millis;
	}

	public boolean isInBusinessHours(Date date) {
		return (findDayPart(date) != null);
	}

	public boolean isHoliday(Date date) {
		if (holidays != null) {
			for (Holiday holiday : holidays) {
				if (holiday.includes(date)) {
					return true;
				}
			}
		}
		return false;
	}

	protected Date findStartOfNextDay(Date date) {
		Calendar calendar = createCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		while (isHoliday(date)) {
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
		}
		return date;
	}

	public Calendar createCalendar() {
		return new GregorianCalendar();
	}

	protected BusinessDay findDay(Date date) {
		Calendar calendar = createCalendar();
		calendar.setTime(date);
		int weekDayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		return days[weekDayIndex];
	}

	protected BusinessDayPart findDayPart(Date date) {
		BusinessDayPart dayPart = null;
		if (!isHoliday(date)) {
			BusinessDay day = findDay(date);
			BusinessDayPart[] dayParts = day.getDayParts();
			if (dayParts != null) {
				for (int i = 0; ((i < dayParts.length) && (dayPart == null)); i++) {
					BusinessDayPart candidate = dayParts[i];
					if (candidate.includes(date)) {
						dayPart = candidate;
					}
				}
			}
		}
		return dayPart;
	}

	protected BusinessDayPart findNextDayPart(Date date) {
		BusinessDayPart nextDayPart = null;
		while (nextDayPart == null) {
			nextDayPart = findDayPart(date);
			if (nextDayPart == null) {
				date = findStartOfNextDay(date);
				Object result[] = new Object[2];
				BusinessDay day = findDay(date);
				day.findNextDayPartStart(0, date, result);
				nextDayPart = (BusinessDayPart) result[1];
			}
		}
		return nextDayPart;
	}

	// getters and setters
	// //////////////////////////////////////////////////////

	public long getBusinessDayInMillis() {
		return businessDayInMillis;
	}

	public void setBusinessDayInMillis(long businessDayInMillis) {
		this.businessDayInMillis = businessDayInMillis;
	}

	public long getBusinessMonthInMillis() {
		return businessMonthInMillis;
	}

	public void setBusinessMonthInMillis(long businessMonthInMillis) {
		this.businessMonthInMillis = businessMonthInMillis;
	}

	public long getBusinessWeekInMillis() {
		return businessWeekInMillis;
	}

	public void setBusinessWeekInMillis(long businessWeekInMillis) {
		this.businessWeekInMillis = businessWeekInMillis;
	}

	public long getBusinessYearInMillis() {
		return businessYearInMillis;
	}

	public void setBusinessYearInMillis(long businessYearInMillis) {
		this.businessYearInMillis = businessYearInMillis;
	}

	public long getDayInMillis() {
		return dayInMillis;
	}

	public void setDayInMillis(long dayInMillis) {
		this.dayInMillis = dayInMillis;
	}

	public BusinessDay[] getDays() {
		return days;
	}

	public void setDays(BusinessDay[] days) {
		this.days = days;
	}

	public Holiday[] getHolidays() {
		return holidays;
	}

	public void setHolidays(Holiday[] holidays) {
		this.holidays = holidays;
	}

	public long getHourInMillis() {
		return hourInMillis;
	}

	public void setHourInMillis(long hourInMillis) {
		this.hourInMillis = hourInMillis;
	}

	public long getMinuteInMillis() {
		return minuteInMillis;
	}

	public void setMinuteInMillis(long minuteInMillis) {
		this.minuteInMillis = minuteInMillis;
	}

	public long getMonthInMillis() {
		return monthInMillis;
	}

	public void setMonthInMillis(long monthInMillis) {
		this.monthInMillis = monthInMillis;
	}

	public long getSecondInMillis() {
		return secondInMillis;
	}

	public void setSecondInMillis(long secondInMillis) {
		this.secondInMillis = secondInMillis;
	}

	public long getWeekInMillis() {
		return weekInMillis;
	}

	public void setWeekInMillis(long weekInMillis) {
		this.weekInMillis = weekInMillis;
	}

	public long getYearInMillis() {
		return yearInMillis;
	}

	public void setYearInMillis(long yearInMillis) {
		this.yearInMillis = yearInMillis;
	}
}

// $Id$