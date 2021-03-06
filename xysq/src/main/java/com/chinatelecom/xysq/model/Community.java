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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.chinatelecom.xysq.api.Selectable;

@Table(name = "COMMUNITY")
@Entity
public class Community implements Selectable{

	private Long id;

	private String name;

	private String pinyin;
	
	private String shortPinyin;

	private String detailAddress;

	private Area city;
	
	private Area area;

	private Set<AdminCommunityRel> adminCommunityRelSet;

	private List<PosterCommunityRel> posterCommunityRelList;

	private Set<StoreCommunityRel> storeCommunityRelSet;

	private boolean selected;

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

	@Column(length = 500)
	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
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
	
	@ManyToOne
	@JoinColumn(name = "AREA_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade = CascadeType.REMOVE)
	public Set<AdminCommunityRel> getAdminCommunityRelSet() {
		return adminCommunityRelSet;
	}

	public void setAdminCommunityRelSet(
			Set<AdminCommunityRel> adminCommunityRelSet) {
		this.adminCommunityRelSet = adminCommunityRelSet;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade = CascadeType.REMOVE)
	@OrderBy("displayIndex")
	public List<PosterCommunityRel> getPosterCommunityRelList() {
		return posterCommunityRelList;
	}

	public void setPosterCommunityRelList(
			List<PosterCommunityRel> posterCommunityRelList) {
		this.posterCommunityRelList = posterCommunityRelList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade = CascadeType.REMOVE)
	public Set<StoreCommunityRel> getStoreCommunityRelSet() {
		return storeCommunityRelSet;
	}

	public void setStoreCommunityRelSet(
			Set<StoreCommunityRel> storeCommunityRelSet) {
		this.storeCommunityRelSet = storeCommunityRelSet;
	}

	@Transient
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
