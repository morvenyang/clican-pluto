package com.clican.irp.android.service;

public interface PropertyService {

	public void createProperty(String name, String value);

	public boolean updateProperty(String name, String value);

	public String getProperty(String name);

	public void deleteProperty(String name);
}
