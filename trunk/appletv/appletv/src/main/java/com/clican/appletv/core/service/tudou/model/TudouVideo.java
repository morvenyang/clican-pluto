package com.clican.appletv.core.service.tudou.model;

import java.io.Serializable;

public class TudouVideo extends ListView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3764310120345910215L;

	private String level;
	private Long totaltime;
	private Long albumId;
	private Long playtimes;
	private String playlistId;
	private Integer ishd;
	private boolean haspwd;
	private Integer origin;
	private Integer pwd;
	private Integer hd;
	private Integer cid;
	private Integer isalbum;
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

	public Long getTotaltime() {
		return totaltime;
	}
	public void setTotaltime(Long totaltime) {
		this.totaltime = totaltime;
	}
	
	public Long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}
	
	public Long getPlaytimes() {
		return playtimes;
	}
	public void setPlaytimes(Long playtimes) {
		this.playtimes = playtimes;
	}
	public String getPlaylistId() {
		return playlistId;
	}
	public void setPlaylistId(String playlistId) {
		this.playlistId = playlistId;
	}
	public Integer getIshd() {
		return ishd;
	}
	public void setIshd(Integer ishd) {
		this.ishd = ishd;
	}
	public boolean isHaspwd() {
		return haspwd;
	}
	public void setHaspwd(boolean haspwd) {
		this.haspwd = haspwd;
	}
	public Integer getHd() {
		return hd;
	}
	public void setHd(Integer hd) {
		this.hd = hd;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public Integer getIsalbum() {
		return isalbum;
	}
	public void setIsalbum(Integer isalbum) {
		this.isalbum = isalbum;
	}
	public Integer getOrigin() {
		return origin;
	}
	public void setOrigin(Integer origin) {
		this.origin = origin;
	}
	public Integer getPwd() {
		return pwd;
	}
	public void setPwd(Integer pwd) {
		this.pwd = pwd;
	}
	
}
