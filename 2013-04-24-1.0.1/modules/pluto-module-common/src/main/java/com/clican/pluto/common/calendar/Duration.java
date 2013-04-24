/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Duration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 865326212663164079L;

	boolean isBusinessTime;
	protected int millis;
	protected int seconds;
	protected int minutes;
	protected int hours;
	protected int days;
	protected int weeks;
	protected int months;
	protected int years;

	/** constructor for persistence. note that this type is to be immutable. */
	protected Duration() {
	}

	/**
	 * parses the duration from a text
	 * 
	 * duration = part [',' part | 'and' part]* part = number ['business'] unit
	 * number = (0..9)+ unit =
	 * (y|year|years|month|months|w|week|weeks|d|day|days
	 * |h|hour|hours|min|minute
	 * |minutes|s|sec|second|seconds|milli|millis|millisecond|milliseconds)
	 * 
	 * @throws JbpmException
	 *             if the parsing is unsuccessful
	 */
	public Duration(String text) {
		if (text == null)
			throw new RuntimeException("text is null");

		for (String part : splitInParts(text)) {
			parsePart(part);
		}

		isBusinessTime = text.indexOf("business") != -1;
	}

	public Duration(boolean isBusinessTime, int millis, int seconds,
			int minutes, int hours, int days, int weeks, int months, int years) {
		this.isBusinessTime = isBusinessTime;
		this.millis = millis;
		this.seconds = seconds;
		this.minutes = minutes;
		this.hours = hours;
		this.days = days;
		this.weeks = weeks;
		this.months = months;
		this.years = years;
	}

	private List<String> splitInParts(String text) {
		List<String> parts = new ArrayList<String>(2);
		while (text != null) {
			int commaIndex = text.indexOf(',');
			int andIndex = text.indexOf(" and ");
			if (((commaIndex == -1) && (andIndex != -1))
					|| (((commaIndex != -1) && (andIndex != -1)) && (andIndex < commaIndex))) {
				String part = text.substring(0, andIndex).trim();
				parts.add(part);
				text = text.substring(andIndex + 5);

			} else if (((commaIndex != -1) && (andIndex == -1))
					|| (((commaIndex != -1) && (andIndex != -1)) && (andIndex > commaIndex))) {
				String part = text.substring(0, commaIndex).trim();
				parts.add(part);
				text = text.substring(commaIndex + 1);

			} else {
				parts.add(text.trim());
				text = null;
			}
		}

		return parts;
	}

	private void parsePart(String part) {
		int spaceIndex = part.indexOf(' ');
		if (spaceIndex == -1) {
			throw new RuntimeException("couldn't parse duration part " + part);
		}
		String quantityText = part.substring(0, spaceIndex).trim();
		spaceIndex = part.lastIndexOf(' ');
		String unitText = part.substring(spaceIndex + 1).trim().toLowerCase();

		int quantity;
		try {
			quantity = Integer.parseInt(quantityText);
		} catch (NumberFormatException e) {
			throw new RuntimeException("couldn't parse quantity "
					+ quantityText + " in duration text", e);
		}
		FieldSetter fieldSetter = fieldSetters.get(unitText);
		if (fieldSetter == null) {
			throw new RuntimeException("couldn't parse quantity "
					+ quantityText);
		}
		fieldSetter.set(this, quantity);
	}

	private interface FieldSetter {
		void set(Duration duration, int quantity);
	}

	private static class MillisSetter implements FieldSetter {
		public void set(Duration duration, int quantity) {
			duration.millis = quantity;
		}
	}

	private static class SecondSetter implements FieldSetter {
		public void set(Duration duration, int quantity) {
			duration.seconds = quantity;
		}
	}

	private static class MinuteSetter implements FieldSetter {
		public void set(Duration duration, int quantity) {
			duration.minutes = quantity;
		}
	}

	private static class HourSetter implements FieldSetter {
		public void set(Duration duration, int quantity) {
			duration.hours = quantity;
		}
	}

	private static class DaySetter implements FieldSetter {
		public void set(Duration duration, int quantity) {
			duration.days = quantity;
		}
	}

	private static class WeekSetter implements FieldSetter {
		public void set(Duration duration, int quantity) {
			duration.weeks = quantity;
		}
	}

	private static class MonthSetter implements FieldSetter {
		public void set(Duration duration, int quantity) {
			duration.months = quantity;
		}
	}

	private static class YearSetter implements FieldSetter {
		public void set(Duration duration, int quantity) {
			duration.years = quantity;
		}
	}

	private static final Map<String, FieldSetter> fieldSetters = new HashMap<String, FieldSetter>();
	static {
		FieldSetter fieldSetter = new MillisSetter();
		fieldSetters.put("milli", fieldSetter);
		fieldSetters.put("millis", fieldSetter);
		fieldSetters.put("millisecond", fieldSetter);
		fieldSetters.put("milliseconds", fieldSetter);

		fieldSetter = new SecondSetter();
		fieldSetters.put("s", fieldSetter);
		fieldSetters.put("sec", fieldSetter);
		fieldSetters.put("second", fieldSetter);
		fieldSetters.put("seconds", fieldSetter);

		fieldSetter = new MinuteSetter();
		fieldSetters.put("min", fieldSetter);
		fieldSetters.put("minute", fieldSetter);
		fieldSetters.put("minutes", fieldSetter);

		fieldSetter = new HourSetter();
		fieldSetters.put("h", fieldSetter);
		fieldSetters.put("hour", fieldSetter);
		fieldSetters.put("hours", fieldSetter);

		fieldSetter = new DaySetter();
		fieldSetters.put("d", fieldSetter);
		fieldSetters.put("day", fieldSetter);
		fieldSetters.put("days", fieldSetter);

		fieldSetter = new WeekSetter();
		fieldSetters.put("w", fieldSetter);
		fieldSetters.put("week", fieldSetter);
		fieldSetters.put("weeks", fieldSetter);

		fieldSetter = new MonthSetter();
		fieldSetters.put("month", fieldSetter);
		fieldSetters.put("months", fieldSetter);

		fieldSetter = new YearSetter();
		fieldSetters.put("y", fieldSetter);
		fieldSetters.put("year", fieldSetter);
		fieldSetters.put("years", fieldSetter);
	}

	public int getDays() {
		return days;
	}

	public int getHours() {
		return hours;
	}

	public boolean isBusinessTime() {
		return isBusinessTime;
	}

	public int getMillis() {
		return millis;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getMonths() {
		return months;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getWeeks() {
		return weeks;
	}

	public int getYears() {
		return years;
	}
}

// $Id$