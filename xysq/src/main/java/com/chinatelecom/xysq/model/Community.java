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

@Table(name = "COMMUNITY")
@Entity
public class Community {
	
	private Long id;
	
	private String name;
	
	private String detailAddress;
	
	private Area region;
	
	private Area city;
	
	private Set<AdminCommunityRel> adminCommunityRelSet;

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
	@Column(length=500)
	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	@ManyToOne
	@JoinColumn(name = "REGION_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Area getRegion() {
		return region;
	}

	public void setRegion(Area region) {
		this.region = region;
	}

	@ManyToOne
	@JoinColumn(name = "CITY_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Area getCity() {
		return city;
	}

	public void setCity(Area city) {
		this.city = city;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade = CascadeType.REMOVE)
	public Set<AdminCommunityRel> getAdminCommunityRelSet() {
		return adminCommunityRelSet;
	}

	public void setAdminCommunityRelSet(Set<AdminCommunityRel> adminCommunityRelSet) {
		this.adminCommunityRelSet = adminCommunityRelSet;
	}
	
	
}
