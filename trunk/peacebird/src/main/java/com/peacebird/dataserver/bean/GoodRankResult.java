package com.peacebird.dataserver.bean;

public class GoodRankResult {

	private String name;
	private Long amount;
	private Integer count;
	private String imageLink;
	private String imageLinkMin;
	private String season;
	private String line;
	private String wave;
	private String year;
	private String colorName;
	
	public GoodRankResult(String name) {
		super();
		this.name = name;
	}

	public GoodRankResult(String name, Number amount, Number count,
			String imageLink, String imageLinkMin,String season,String line,String wave,String year) {
		this.name = name;
		if(amount!=null){
			this.amount = amount.longValue();
		}
		if(count!=null){
			this.count = count.intValue();
		}
		this.imageLink = imageLink;
		this.imageLinkMin = imageLinkMin;
		this.season = season;
		this.line = line;
		this.wave = wave;
		this.year = year;
	}
	
	public GoodRankResult(String name, Number amount, Number count,
			String imageLink, String imageLinkMin,String season,String line,String wave,String year,String colorName) {
		this.name = name;
		if(amount!=null){
			this.amount = amount.longValue();
		}
		if(count!=null){
			this.count = count.intValue();
		}
		this.imageLink = imageLink;
		this.imageLinkMin = imageLinkMin;
		this.season = season;
		this.line = line;
		this.wave = wave;
		this.year = year;
		this.colorName = colorName;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public String getImageLinkMin() {
		return imageLinkMin;
	}
	public void setImageLinkMin(String imageLinkMin) {
		this.imageLinkMin = imageLinkMin;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getWave() {
		return wave;
	}

	public void setWave(String wave) {
		this.wave = wave;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	

	
	
}
