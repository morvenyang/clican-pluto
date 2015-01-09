package com.chinatelecom.xysq.model;

import java.util.Date;
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
	
	private Date createTime;
	
	private Date modifyTime;

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

	@Column
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
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
		Store other = (Store) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
