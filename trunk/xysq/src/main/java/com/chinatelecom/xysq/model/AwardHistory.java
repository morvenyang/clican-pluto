package com.chinatelecom.xysq.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

@Table(name = "AWARD_HISTORY")
@Entity
public class AwardHistory {

	
	private Long id;
	
	private Award award;
	
	private boolean lottery;
	
	private Date date;
	
	private User user;
	
	private int money;
	
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

	@Column(name = "LOTTERY")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isLottery() {
		return lottery;
	}

	public void setLottery(boolean lottery) {
		this.lottery = lottery;
	}

	@Column(name="DATE")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name="MONEY")
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	
}
