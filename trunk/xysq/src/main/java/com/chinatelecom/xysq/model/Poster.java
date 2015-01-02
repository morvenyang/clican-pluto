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
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.chinatelecom.xysq.enumeration.InnerModule;
import com.chinatelecom.xysq.enumeration.PosterType;


@Table(name = "POSTER")
@Entity
public class Poster {

	private Long id;

	private Community community;
	
	private Image image;

	private PosterType type;

	private String html5Link;

	private InnerModule innerModule;

	private Store store;

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
	@JoinColumn(name = "COMMUNITY_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
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
	@Column(name = "TYPE")
	@Type(type = "com.chinatelecom.xysq.hibernate.EnumerationType", parameters = { @Parameter(name = "enumClass", value = "com.chinatelecom.xysq.enumeration.PosterType") })
	public PosterType getType() {
		return type;
	}

	public void setType(PosterType type) {
		this.type = type;
	}

	@Column
	public String getHtml5Link() {
		return html5Link;
	}

	public void setHtml5Link(String html5Link) {
		this.html5Link = html5Link;
	}
	
	@Column(name = "INNER_MODULE")
	@Type(type = "com.chinatelecom.xysq.hibernate.EnumerationType", parameters = { @Parameter(name = "enumClass", value = "com.chinatelecom.xysq.enumeration.InnerModule") })
	public InnerModule getInnerModule() {
		return innerModule;
	}

	public void setInnerModule(InnerModule innerModule) {
		this.innerModule = innerModule;
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
	
	

	
}
