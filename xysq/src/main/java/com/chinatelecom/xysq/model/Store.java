package com.chinatelecom.xysq.model;

import java.util.List;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Table(name = "STORE")
@Entity
public class Store {

	private Long id;

	private String name;
	
	private String pinyin;
	
	private String shortPinyin;

	private String address;

	private String tel;

	private String description;

	private List<Image> images;

	private User owner;

	private Set<StoreCommunityRel> storeCommunityRelSet;

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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	@Column
	public String getShortPinyin() {
		return shortPinyin;
	}

	public void setShortPinyin(String shortPinyin) {
		this.shortPinyin = shortPinyin;
	}

	@Column
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(length = 4000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "store", cascade = CascadeType.REMOVE)
	@OrderBy("seq")
	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	@ManyToOne
	@JoinColumn(name = "OWNER_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "store", cascade = CascadeType.REMOVE)
	public Set<StoreCommunityRel> getStoreCommunityRelSet() {
		return storeCommunityRelSet;
	}

	public void setStoreCommunityRelSet(Set<StoreCommunityRel> storeCommunityRelSet) {
		this.storeCommunityRelSet = storeCommunityRelSet;
	}

}
