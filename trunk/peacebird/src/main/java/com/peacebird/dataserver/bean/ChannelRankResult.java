package com.peacebird.dataserver.bean;

import java.util.List;

public class ChannelRankResult implements Comparable<ChannelRankResult>{
	
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
	
	private int getIndex() {
		if (channel.equals("自营") || channel.equals("直营")) {
			return 1;
		} else if (channel.equals("加盟")) {
			return 2;
		} else if (channel.equals("联营")) {
			return 3;
		} else if (channel.equals("分销")) {
			return 4;
		} else if (channel.equals("魔法")) {
			return 5;
		} else if (channel.equals("其他")) {
			return 6;
		} else {
			return 6;
		}
	}

	@Override
	public int compareTo(ChannelRankResult o) {
		int diff = this.getIndex() - o.getIndex();
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else {
			return 0;
		}
	}

}
