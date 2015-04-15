package com.chinatelecom.xysq.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Table(name = "AWARD_STORE_REL")
@Entity
public class AwardStoreRel {

	
	private Long id;
	
	private Award award;
	
	private Store store;
	
	private int amount;
	
	private int totalAmount;
	
	private Set<AwardHistory> awardHistorySet;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "AWARD_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	@ManyToOne
	@JoinColumn(name = "STORE_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@Column(name="AMOUNT")
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Column(name="TOTAL_AMOUNT")
	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "awardStoreRel", cascade = CascadeType.REMOVE)
	public Set<AwardHistory> getAwardHistorySet() {
		return awardHistorySet;
	}

	public void setAwardHistorySet(Set<AwardHistory> awardHistorySet) {
		this.awardHistorySet = awardHistorySet;
	}
	
	
}
