package com.peacebird.dataserver.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Table(name = "DAY_RETAIL_CHANNEL")
@Entity
public class DayRetailChannel {
	private Long id;
	private String brand;
	private String channel;
	private Date date;
	private Integer dayTarget;
	private Integer dayAmount;
	private Integer weekAmount;
	private Integer yearAmount;
	private Double dayLike;
	private Double weekLike;
	private Double yearLike;
	
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
	@Column(name="s_channel")
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	@Column(name="m_date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(name="r_day_target")
	public Integer getDayTarget() {
		return dayTarget;
	}
	public void setDayTarget(Integer dayTarget) {
		this.dayTarget = dayTarget;
	}
	@Column(name="r_day_amount")
	public Integer getDayAmount() {
		return dayAmount;
	}
	public void setDayAmount(Integer dayAmount) {
		this.dayAmount = dayAmount;
	}
	@Column(name="r_week_amount")
	public Integer getWeekAmount() {
		return weekAmount;
	}
	public void setWeekAmount(Integer weekAmount) {
		this.weekAmount = weekAmount;
	}
	@Column(name="r_year_amount")
	public Integer getYearAmount() {
		return yearAmount;
	}
	public void setYearAmount(Integer yearAmount) {
		this.yearAmount = yearAmount;
	}
	@Column(name="r_week_like")
	public Double getWeekLike() {
		return weekLike;
	}
	public void setWeekLike(Double weekLike) {
		this.weekLike = weekLike;
	}
	@Column(name="r_year_like")
	public Double getYearLike() {
		return yearLike;
	}
	public void setYearLike(Double yearLike) {
		this.yearLike = yearLike;
	}
	@Column(name="r_day_like")
	public Double getDayLike() {
		return dayLike;
	}
	public void setDayLike(Double dayLike) {
		this.dayLike = dayLike;
	}
	
	
	
}
