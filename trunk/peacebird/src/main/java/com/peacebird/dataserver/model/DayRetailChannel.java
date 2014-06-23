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
	private Long dayTarget;
	private Long dayAmount;
	private Long monthAmount;
	private Long weekAmount;
	private Long yearAmount;
	private Double dayLike;
	private Double weekLike;
	private Double monthLike;
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
	public Long getDayTarget() {
		return dayTarget;
	}
	public void setDayTarget(Long dayTarget) {
		this.dayTarget = dayTarget;
	}
	@Column(name="r_day_amount")
	public Long getDayAmount() {
		return dayAmount;
	}
	public void setDayAmount(Long dayAmount) {
		this.dayAmount = dayAmount;
	}
	@Column(name="r_week_amount")
	public Long getWeekAmount() {
		return weekAmount;
	}
	public void setWeekAmount(Long weekAmount) {
		this.weekAmount = weekAmount;
	}
	@Column(name="r_year_amount")
	public Long getYearAmount() {
		return yearAmount;
	}
	public void setYearAmount(Long yearAmount) {
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
	@Column(name="r_month_amount")
	public Long getMonthAmount() {
		return monthAmount;
	}
	public void setMonthAmount(Long monthAmount) {
		this.monthAmount = monthAmount;
	}
	@Column(name="r_month_like")
	public Double getMonthLike() {
		return monthLike;
	}
	public void setMonthLike(Double monthLike) {
		this.monthLike = monthLike;
	}
	
	
	
}
