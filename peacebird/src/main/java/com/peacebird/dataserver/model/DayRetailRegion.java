package com.peacebird.dataserver.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DAY_RETAIL_REGION")
@Entity
public class DayRetailRegion {
	private Long id;
	private String brand;
	private String region;
	private Date date;
	private Integer dayAmount;
	
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
	@Column(name="r_region")
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	@Column(name="m_date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(name="r_day_amount")
	public Integer getDayAmount() {
		return dayAmount;
	}
	public void setDayAmount(Integer dayAmount) {
		this.dayAmount = dayAmount;
	}
	
	
}
