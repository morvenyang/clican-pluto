package com.ikidstv.quiz.model;

import java.lang.reflect.Method;

import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Metadata {

	private final static Log log = LogFactory.getLog(Metadata.class);
	
	public abstract Long getId();
	
	@Transient
	public String getRecord(int index) {
		try {
			Method method = this.getClass().getMethod("getRecord" + index,
					new Class[] {});
			return (String) method.invoke(this, new Object[] {});
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}
	
	@Transient
	public String getWord(int index) {
		try {
			Method method = this.getClass().getMethod("getWord" + index,
					new Class[] {});
			return (String) method.invoke(this, new Object[] {});
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}
	
	@Transient
	public String getPicture(int index) {
		try {
			Method method = this.getClass().getMethod("getPicture" + index,
					new Class[] {});
			return (String)method.invoke(this, new Object[] {});
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}
	
	@Transient
	public Boolean getAnswer(int index) {
		try {
			Method method = this.getClass().getMethod("isAnswer" + index,
					new Class[] {});
			return (Boolean)method.invoke(this, new Object[] {});
		} catch (Exception e) {
			log.error("",e);
		}
		return false;
	}
	
	@Transient
	public Integer getX(int index) {
		try {
			Method method = this.getClass().getMethod("getX" + index,
					new Class[] {});
			return (Integer)method.invoke(this, new Object[] {});
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}
	
	@Transient
	public Integer getY(int index) {
		try {
			Method method = this.getClass().getMethod("getY" + index,
					new Class[] {});
			return (Integer)method.invoke(this, new Object[] {});
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}
	
}
