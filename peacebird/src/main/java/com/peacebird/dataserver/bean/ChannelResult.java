package com.peacebird.dataserver.bean;

public class ChannelResult {

	
	private Integer dayAmount;
	
	private Double docNumber;
	
	private Double avgDocCount;
	
	private Integer avgPrice;
	
	private Integer aps;
	
	private String channel;

	public ChannelResult(Number dayAmount, Number docNumber,
			Number avgDocCount, Number avgPrice, Number aps, String channel) {
		if(dayAmount!=null){
			this.dayAmount = dayAmount.intValue();
		}
		if(docNumber!=null){
			this.docNumber = docNumber.doubleValue();
		}
		if(avgDocCount!=null){
			this.avgDocCount = avgDocCount.doubleValue();
		}
		if(avgPrice!=null){
			this.avgPrice = avgPrice.intValue();
		}
		if(aps!=null){
			this.aps = aps.intValue();
		}
		this.channel = channel;
	}

	public Integer getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(Integer dayAmount) {
		this.dayAmount = dayAmount;
	}

	public Double getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(Double docNumber) {
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
	
	
}
