package com.peacebird.dataserver.bean;

public class ChannelResult implements Comparable<ChannelResult> {

	private Long dayAmount;

	private Integer docNumber;

	private Double avgDocCount;

	private Integer avgPrice;

	private Integer aps;

	private String channel;

	public ChannelResult(Number dayAmount, Number docNumber,
			Number avgDocCount, Number avgPrice, Number aps, String channel) {
		if (dayAmount != null) {
			this.dayAmount = dayAmount.longValue();
		}
		if (docNumber != null) {
			this.docNumber = docNumber.intValue();
		}
		if (avgDocCount != null) {
			this.avgDocCount = avgDocCount.doubleValue();
		}
		if (avgPrice != null) {
			this.avgPrice = avgPrice.intValue();
		}
		if (aps != null) {
			this.aps = aps.intValue();
		}
		this.channel = channel;
	}

	public Long getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(Long dayAmount) {
		this.dayAmount = dayAmount;
	}

	public Integer getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(Integer docNumber) {
		this.docNumber = docNumber;
	}

	public Double getAvgDocCount() {
		return avgDocCount;
	}

	public void setAvgDocCount(Double avgDocCount) {
		this.avgDocCount = avgDocCount;
	}

	public Integer getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(Integer avgPrice) {
		this.avgPrice = avgPrice;
	}

	public Integer getAps() {
		return aps;
	}

	public void setAps(Integer aps) {
		this.aps = aps;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	private int getIndex() {
		if (channel.equals("全部")) {
			return 0;
		} else if (channel.equals("自营") || channel.equals("直营")) {
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
		} else if (channel.equals("电商")) {
			return 7;
		} else {
			return 8;
		}
	}

	@Override
	public int compareTo(ChannelResult o) {
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
