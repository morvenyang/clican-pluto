package com.huace.mas.entity;

import java.io.Serializable;


public class Project implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3098066377647010618L;
	//配置文件节点
	public static String P_name = "name";
	public static String P_id = "id";
	
	private int id;//工程ID
	private String projectName;//工程名称
	private int surfNum;//工程所属表面位移监测点个数
	private int reseNum;//工程所属库水位监测点个数
	private int satuNum;//工程所属浸润线监测移点个数
	
	private int innerNum;//工程所属内部位移移点个数
	private int rainNum;//工程所属雨量监测点个数
	private int flowNum;//工程所属渗流监测点个数
	private int beachNum;//工程所属干滩监测点个数
	
	
	
	public int getInnerNum() {
		return innerNum;
	}
	public void setInnerNum(int innerNum) {
		this.innerNum = innerNum;
	}
	public int getRainNum() {
		return rainNum;
	}
	public void setRainNum(int rainNum) {
		this.rainNum = rainNum;
	}
	public int getFlowNum() {
		return flowNum;
	}
	public void setFlowNum(int flowNum) {
		this.flowNum = flowNum;
	}
	public int getBeachNum() {
		return beachNum;
	}
	public void setBeachNum(int beachNum) {
		this.beachNum = beachNum;
	}
	public int getSurfNum() {
		return surfNum;
	}
	public void setSurfNum(int surfNum) {
		this.surfNum = surfNum;
	}
	public int getReseNum() {
		return reseNum;
	}
	public void setReseNum(int reseNum) {
		this.reseNum = reseNum;
	}
	public int getSatuNum() {
		return satuNum;
	}
	public void setSatuNum(int satuNum) {
		this.satuNum = satuNum;
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
	
	
}
