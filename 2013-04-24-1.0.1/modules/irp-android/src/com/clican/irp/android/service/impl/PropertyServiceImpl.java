package com.clican.irp.android.service.impl;

import com.clican.irp.android.db.PropertyDbAdapter;
import com.clican.irp.android.service.PropertyService;
import com.google.inject.Inject;

public class PropertyServiceImpl implements PropertyService {

	@Inject
	private PropertyDbAdapter propertyDbAdapter;

	@Override
	public void createProperty(String name, String value) {
		try {
			propertyDbAdapter.open();
			propertyDbAdapter.createProperty(name, value);
		} finally {
			propertyDbAdapter.close();
		}
	}

	@Override
	public boolean updateProperty(String name, String value) {
		try {
			propertyDbAdapter.open();
			return propertyDbAdapter.updateProperty(name, value);
		} finally {
			propertyDbAdapter.close();
		}
	}

	@Override
	public String getProperty(String name) {
		try {
			propertyDbAdapter.open();
			return propertyDbAdapter.getProperty(name);
		} finally {
			propertyDbAdapter.close();
		}
	}

	@Override
	public void deleteProperty(String name) {
		try {
			propertyDbAdapter.open();
			propertyDbAdapter.deleteProperty(name);
		} finally {
			propertyDbAdapter.close();
		}

	}

}
