package com.chinatelecom.xysq.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "BROADBAND_REMIND")
@Entity
public class BroadbandRemind {

	private Long id;
	
	private String msisidn;
	
	private Date expiredDate;
	
	private String broadBandId;
	
	private String userName;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column
	public String getMsisidn() {
		return msisidn;
	}

	public void setMsisidn(String msisidn) {
		this.msisidn = msisidn;
	}
	@Column
	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	@Column
	public String getBroadBandId() {
		return broadBandId;
	}

	public void setBroadBandId(String broadBandId) {
		this.broadBandId = broadBandId;
	}
	@Column
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
