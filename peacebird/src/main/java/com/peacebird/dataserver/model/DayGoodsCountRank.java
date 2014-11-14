package com.peacebird.dataserver.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "DAY_GOODS_COUNT_RANK")
@Entity
public class DayGoodsCountRank {
	
	private Long id;
	private String brand;
	private String name;
	private Date date;
	private String season;
	private String line;
	private String wave;
	private Integer count;
	private Long amount;
	private String year;
	private String rank;
	private String imageLink;
	private String imageLinkMin;
	private String colorName;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="b_brand")
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	@Column(name="g_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="m_date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(name="g_season")
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	@Column(name="g_line")
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	@Column(name="g_wave")
	public String getWave() {
		return wave;
	}
	public void setWave(String wave) {
		this.wave = wave;
	}
	@Column(name="r_count")
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Column(name="r_amount")
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	@Column(name="rank")
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	@Column(name = "g_imglink")
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	@Column(name = "g_imglink_min")
	public String getImageLinkMin() {
		return imageLinkMin;
	}
	public void setImageLinkMin(String imageLinkMin) {
		this.imageLinkMin = imageLinkMin;
	}
	@Column(name = "g_year")
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	@Column(name = "g_colorname")
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
}
