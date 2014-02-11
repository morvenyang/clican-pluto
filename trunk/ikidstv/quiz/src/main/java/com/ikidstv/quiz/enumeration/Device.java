package com.ikidstv.quiz.enumeration;

public enum Device {

	IPhone, IPad, Android;

	public static Device convert(String device) {
		for (Device member : values()) {
			if (member.name().equalsIgnoreCase(device)) {
				return member;
			}
		}
		return null;
	}
}
