package com.huace.mas.entity;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1548891284077535230L;
	
	private int userID ; //用户ID
	private String userName ;//用户名
	private String pwd ;//用户密码
	private String realName ;//用户真名
	private String callPhoneNo ;//用户手机号
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCallPhoneNo() {
		return callPhoneNo;
	}
	public void setCallPhoneNo(String callPhoneNo) {
		this.callPhoneNo = callPhoneNo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
