package com.peacebird.dataserver.bean;

import java.util.List;

import com.peacebird.dataserver.model.DataRetailsNoRetail;

public class DataRetailsChannelNoRetailResult {

	private String channel;
	
	private List<DataRetailsNoRetail> noRetails;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public List<DataRetailsNoRetail> getNoRetails() {
		return noRetails;
	}

	public void setNoRetails(List<DataRetailsNoRetail> noRetails) {
		this.noRetails = noRetails;
	}
	
	
}
