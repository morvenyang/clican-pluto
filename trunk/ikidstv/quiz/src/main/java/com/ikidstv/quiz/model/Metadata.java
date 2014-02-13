package com.ikidstv.quiz.model;

import java.lang.reflect.Method;

import javax.persistence.Transient;

public abstract class Metadata {

	public abstract Long getId();
	
	@Transient
	public String getRecord(int index) {
		try {
			Method method = Bingo.class.getMethod("getRecord" + index,
					new Class[] {});
			return (String) method.invoke(this, new Object[] {});
		} catch (Exception e) {

		}
		return null;
	}
	
	@Transient
	public String getWord(int index) {
		try {
			Method method = Bingo.class.getMethod("getWord" + index,
					new Class[] {});
			return (String) method.invoke(this, new Object[] {});
		} catch (Exception e) {

		}
		return null;
	}
	
	@Transient
	public String getPicture(int index) {
		try {
			Method method = Bingo.class.getMethod("getPicture" + index,
					new Class[] {});
			return (String) method.invoke(this, new Object[] {});
		} catch (Exception e) {

		}
		return null;
	}
	
}
