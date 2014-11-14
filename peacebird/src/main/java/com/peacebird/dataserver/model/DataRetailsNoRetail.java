package com.peacebird.dataserver.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "DATA_RETAILS_NORETAIL")
@Entity
public class DataRetailsNoRetail {
	
	private Long id;
	
	private Date date;
	
	private String brand;
	
	private String storeCode;
	
	private String storeName;
	
	private Long nodays;
	
	private Date writetime;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "m_date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "b_brand")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name = "storecode")
	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	@Column(name = "storename")
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Column(name = "nodays")
	public Long getNodays() {
		return nodays;
	}

	public void setNodays(Long nodays) {
		this.nodays = nodays;
	}
	
	@Column(name = "d_writetime")
	public Date getWritetime() {
		return writetime;
	}

	public void setWritetime(Date writetime) {
		this.writetime = writetime;
	}
	
	

}
