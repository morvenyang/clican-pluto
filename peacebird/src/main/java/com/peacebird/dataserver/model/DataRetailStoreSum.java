package com.peacebird.dataserver.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DATA_RETAIL_STORESUM")
@Entity
public class DataRetailStoreSum {

	private Long id;
	
	private Date date;
	
	private String type;
	
	private String sumType;
	
	private Long total;
	
	private Long self;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "d_date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "d_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "d_sumtype")
	public String getSumType() {
		return sumType;
	}

	public void setSumType(String sumType) {
		this.sumType = sumType;
	}

	@Column(name = "m_total")
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Column(name = "m_self")
	public Long getSelf() {
		return self;
	}

	public void setSelf(Long self) {
		this.self = self;
	}

	@Column(name = "m_join")
	public Long getJoin() {
		return join;
	}

	public void setJoin(Long join) {
		this.join = join;
	}

	@Column(name = "m_union")
	public Long getUnion() {
		return union;
	}

	public void setUnion(Long union) {
		this.union = union;
	}
	@Column(name = "d_writetime")
	public Date getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}

	private Long join;
	
	private Long union;
	
	private Date writeTime;
	
	
}
