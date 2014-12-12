package com.peacebird.dataserver.bean;

import java.util.List;

import com.peacebird.dataserver.model.DataRetailsNoRetail;

public class DataRetailsChannelNoRetailResult {

	private String channel;
	
	private List<DataRetailsNoRetail> stores;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public List<DataRetailsNoRetail> getStores() {
		return stores;
	}

	public void setStores(List<DataRetailsNoRetail> stores) {
		this.stores = stores;
	}

	
	
	
}
