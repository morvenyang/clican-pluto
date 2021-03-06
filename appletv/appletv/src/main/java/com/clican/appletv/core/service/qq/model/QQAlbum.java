package com.clican.appletv.core.service.qq.model;

import java.util.List;

public class QQAlbum {

	private String actor;
	private String area;
	private String dctor;
	private String desc;
	private String id;
	private String pic;
	private String score;
	private String tt;
	private String subTt;
	private int year;
	private List<QQAlbumItem> albumItems;
	private int size;
	private Integer isalbum=1;
	
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDctor() {
		return dctor;
	}
	public void setDctor(String dctor) {
		this.dctor = dctor;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getTt() {
		return tt;
	}
	public void setTt(String tt) {
		this.tt = tt;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public List<QQAlbumItem> getAlbumItems() {
		return albumItems;
	}
	public void setAlbumItems(List<QQAlbumItem> albumItems) {
		this.albumItems = albumItems;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getSubTt() {
		return subTt;
	}
	public void setSubTt(String subTt) {
		this.subTt = subTt;
	}
	public Integer getIsalbum() {
		return isalbum;
	}
	public void setIsalbum(Integer isalbum) {
		this.isalbum = isalbum;
	}
	
	
}
