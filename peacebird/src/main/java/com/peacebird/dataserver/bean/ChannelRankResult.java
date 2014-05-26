package com.peacebird.dataserver.bean;

import java.util.List;

public class ChannelRankResult {
	
	private List<RankResult> ranks;
	
	private String channel;

	public List<RankResult> getRanks() {
		return ranks;
	}

	public void setRanks(List<RankResult> ranks) {
		this.ranks = ranks;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	

}
