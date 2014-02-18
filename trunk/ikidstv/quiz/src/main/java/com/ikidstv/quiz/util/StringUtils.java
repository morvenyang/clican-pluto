package com.ikidstv.quiz.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

	
	public static String generateFilePathByDate(){
		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH");
		return sdf.format(current);
	}
	
	public static void main(String[] args){
		System.out.println(generateFilePathByDate());
	}
}
