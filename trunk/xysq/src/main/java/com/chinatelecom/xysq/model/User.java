package com.chinatelecom.xysq.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.chinatelecom.xysq.enumeration.Role;

@Table(name = "USERS")
@Entity
public class User {

	
	private Long id;
	private String userName;
	private String password;
	private String confirmedPassword;
	private String nickName;
	private boolean active = true;
	private Role role;
	private String msisdn;
	private String address;
	private String carNumber;
	private Set<AdminCommunityRel> adminCommunityRelSet;
	private boolean applyXqnc;
	private Integer money;
	private Integer lottery;

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
	
	@Column(name = "ACTIVE")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@Column(name = "ROLE")
	@Type(type = "com.chinatelecom.xysq.hibernate.EnumerationType", parameters = { @Parameter(name = "enumClass", value = "com.chinatelecom.xysq.enumeration.Role") })
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Column
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	@Column
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin", cascade = CascadeType.REMOVE)
	public Set<AdminCommunityRel> getAdminCommunityRelSet() {
		return adminCommunityRelSet;
	}
	public void setAdminCommunityRelSet(Set<AdminCommunityRel> adminCommunityRelSet) {
		this.adminCommunityRelSet = adminCommunityRelSet;
	}
	@Column
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	@Column(name = "APPLY_XQNC")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isApplyXqnc() {
		return applyXqnc;
	}
	public void setApplyXqnc(boolean applyXqnc) {
		this.applyXqnc = applyXqnc;
	}
	@Column(name = "MONEY")
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	@Column(name = "LOTTERY")
	public Integer getLottery() {
		return lottery;
	}
	public void setLottery(Integer lottery) {
		this.lottery = lottery;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
