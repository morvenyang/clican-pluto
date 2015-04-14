package com.chinatelecom.xysq.model;

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


@Table(name = "AWARD")
@Entity
public class Award {

	private Long id;
	
	private String name;
	
	private int amount;
	
	private int cost;
	
	private Image image;
	

	private String storeName;
	
	private String storeAddress;
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
	@Column(name = "AMOUNT")
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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
	@Column(name = "STORE_NAME")
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	@Column(name = "STORE_ADDRESS")
	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	
	
}
