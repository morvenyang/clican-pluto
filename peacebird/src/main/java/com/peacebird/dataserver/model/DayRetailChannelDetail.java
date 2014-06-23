package com.peacebird.dataserver.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "DAY_RETAIL_CHANNEL_DETAIL")
@Entity
public class DayRetailChannelDetail {
	private Long id;
	private String brand;
	private String channel;
	private Date date;
	private Integer docNum;
	private Double avgDocCount;
	private Long avgDocAmount;
	private Integer avgPrice;
	private Integer aps;
	
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
	@Column(name="r_doc_num")
	public Integer getDocNum() {
		return docNum;
	}
	public void setDocNum(Integer docNum) {
		this.docNum = docNum;
	}
	
	@Column(name="r_avg_doc_count")
	public Double getAvgDocCount() {
		return avgDocCount;
	}
	public void setAvgDocCount(Double avgDocCount) {
		this.avgDocCount = avgDocCount;
	}
	@Column(name="r_avg_doc_amount")
	public Long getAvgDocAmount() {
		return avgDocAmount;
	}

	public void setAvgDocAmount(Long avgDocAmount) {
		this.avgDocAmount = avgDocAmount;
	}
	@Column(name="r_avg_price")
	public Integer getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(Integer avgPrice) {
		this.avgPrice = avgPrice;
	}
	@Column(name="r_APS")
	public Integer getAps() {
		return aps;
	}
	public void setAps(Integer aps) {
		this.aps = aps;
	}
	
	
}
