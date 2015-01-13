package com.chinatelecom.xysq.model;

import java.util.Date;
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
	
	private Date createTime;
	
	private Date modifyTime;
	
	private boolean defaultPoster;

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

	@Column(name = "DEFAULT_POSTER")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isDefaultPoster() {
		return defaultPoster;
	}

	public void setDefaultPoster(boolean defaultPoster) {
		this.defaultPoster = defaultPoster;
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
		Poster other = (Poster) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
