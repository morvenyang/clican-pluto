package com.peacebird.dataserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;


@Table(name = "USERS")
@Entity
public class User {

	private Long id;
	private String userName;
	private String password;
	private String confirmedPassword;
	private int expiredDays;
	private boolean active = true;
	private int role = 1; //1客户端 2管理员
	private String brands;
	private String token;
	private Integer timeoutInterval=60;
	private boolean reset = true;
	private String remark;
	private String tel;
	
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Transient
	public String getConfirmedPassword() {
		return confirmedPassword;
	}
	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}
	@Column
	public int getExpiredDays() {
		return expiredDays;
	}
	public void setExpiredDays(int expiredDays) {
		this.expiredDays = expiredDays;
	}
	@Column
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	@Column(name = "ACTIVE")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@Column
	public String getBrands() {
		return brands;
	}
	public void setBrands(String brands) {
		this.brands = brands;
	}
	@Column
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Column
	public Integer getTimeoutInterval() {
		return timeoutInterval;
	}
	public void setTimeoutInterval(Integer timeoutInterval) {
		this.timeoutInterval = timeoutInterval;
	}
	@Column(name = "RESET")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isReset() {
		return reset;
	}
	public void setReset(boolean reset) {
		this.reset = reset;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
}
