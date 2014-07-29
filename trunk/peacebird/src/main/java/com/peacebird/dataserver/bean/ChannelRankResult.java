package com.peacebird.dataserver.bean;

import java.util.List;

public class ChannelRankResult{
	
	private List<StoreRankResult> ranks;
	
	private String channel;

	public List<StoreRankResult> getRanks() {
		return ranks;
	}

	public void setRanks(List<StoreRankResult> ranks) {
		this.ranks = ranks;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
