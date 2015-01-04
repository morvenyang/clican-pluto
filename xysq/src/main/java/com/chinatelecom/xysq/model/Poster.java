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
import javax.persistence.Transient;

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

	private String name;

	private Set<PosterCommunityRel> posterCommunityRelSet;

	private Image image;

	private PosterType type;

	private String html5Link;

	private InnerModule innerModule;

	private Store store;

	private Long storeId;

	private boolean global;

	private User owner;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "poster", cascade = CascadeType.REMOVE)
	public Set<PosterCommunityRel> getPosterCommunityRelSet() {
		return posterCommunityRelSet;
	}

	public void setPosterCommunityRelSet(
			Set<PosterCommunityRel> posterCommunityRelSet) {
		this.posterCommunityRelSet = posterCommunityRelSet;
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

	@Column(name = "GLOBAL")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
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

	@Transient
	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

}
