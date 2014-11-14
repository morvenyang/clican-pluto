package com.peacebird.dataserver.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DATA_RETAIL_CHANNEL")
@Entity
public class DataRetailChannel {

	private Long id;
	
	/**
	 * days, weeks, months
	 */
	private String dateType;
	
	private String dateValue;
	
	private Date date;
	
	private String brand;
	
	private String channel;
	
	private Long amount;
	
	private Long preAmount;
	
	private Date writeTime;
	
	private String writeType;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="d_datetype")
	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	@Column(name="d_datevalue")
	public String getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	@Column(name="d_date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column(name="d_brand")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name="d_channel")
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Column(name="m_amount")
	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	@Column(name="m_preamount")
	public Long getPreAmount() {
		return preAmount;
	}

	public void setPreAmount(Long preAmount) {
		this.preAmount = preAmount;
	}

	@Column(name="d_writetime")
	public Date getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	
	@Column(name="d_writetype")
	public String getWriteType() {
		return writeType;
	}

	public void setWriteType(String writeType) {
		this.writeType = writeType;
	}
	
	
}
