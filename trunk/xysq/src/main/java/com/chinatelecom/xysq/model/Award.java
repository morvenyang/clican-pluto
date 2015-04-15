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
import org.hibernate.annotations.Type;


@Table(name = "AWARD")
@Entity
public class Award {

	private Long id;
	
	private String name;
	
	private int cost;
	
	private Image image;
	
	private boolean active;
	
	private boolean realGood;
	
	private Set<AwardStoreRel> awardStoreRelSet;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "COST")
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	@ManyToOne
	@JoinColumn(name = "IMAGE_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	@Column(name = "ACTIVE")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "award", cascade = CascadeType.REMOVE)
	public Set<AwardStoreRel> getAwardStoreRelSet() {
		return awardStoreRelSet;
	}

	public void setAwardStoreRelSet(Set<AwardStoreRel> awardStoreRelSet) {
		this.awardStoreRelSet = awardStoreRelSet;
	}

	@Column(name = "REAL_GOOD")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isRealGood() {
		return realGood;
	}

	public void setRealGood(boolean realGood) {
		this.realGood = realGood;
	}
	
	
}
