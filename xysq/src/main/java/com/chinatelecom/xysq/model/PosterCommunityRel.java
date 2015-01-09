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

@Table(name = "POSTER_COMMUNITY_REL")
@Entity
public class PosterCommunityRel {

	private Long id;
	
	private Poster poster;
	
	private Community community;
	
	private int displayIndex;

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
	@JoinColumn(name = "POSTER_ID", nullable = false)
	@Fetch(FetchMode.JOIN)
	public Poster getPoster() {
		return poster;
	}

	public void setPoster(Poster poster) {
		this.poster = poster;
	}

	@ManyToOne
	@JoinColumn(name = "COMMUNITY_ID", nullable = false)
	@Fetch(FetchMode.JOIN)
	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	@Column
	public int getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(int displayIndex) {
		this.displayIndex = displayIndex;
	}

	
	
	
}
