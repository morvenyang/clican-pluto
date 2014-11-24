package com.peacebird.dataserver.bean;

import java.util.List;

public class ChannelRankResult{
	
	private List<StoreRankResult> ranks;
	
	private List<StoreRankResult> reverseRanks;
	
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

	public List<StoreRankResult> getReverseRanks() {
		return reverseRanks;
	}

	public void setReverseRanks(List<StoreRankResult> reverseRanks) {
		this.reverseRanks = reverseRanks;
	}
	
}
