package com.clican.appletv.common;

public enum Provider {

	TODOU("tudou"),

	QQ("qq");

	private String value;

	private Provider(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static Provider convert(String value) {
		for (Provider provider : values()) {
			if (provider.getValue().equalsIgnoreCase(value)) {
				return provider;
			}
		}
		return null;
	}
}
