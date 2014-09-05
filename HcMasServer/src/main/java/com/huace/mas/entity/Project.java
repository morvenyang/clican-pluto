package com.huace.mas.entity;

import java.io.Serializable;
import java.util.List;


public class Project implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3098066377647010618L;
	//配置文件节点
	
	private int id;//工程ID
	private String projectName;//工程名称
	
	private String __type="Project:#Shhc.Mass.ClassLibrary.entity";
	
	private List<String> kpis;
	
	public String get__type() {
		return __type;
	}
	public void set__type(String __type) {
		this.__type = __type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public List<String> getKpis() {
		return kpis;
	}
	public void setKpis(List<String> kpis) {
		this.kpis = kpis;
	}
	
	
}
