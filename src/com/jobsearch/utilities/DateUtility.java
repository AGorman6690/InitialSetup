package com.jobsearch.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class DateUtility {
	
	public static java.sql.Date getSqlDate(String input){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = sdf.parse(input);
			return new java.sql.Date(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static java.sql.Date getSqlDate(String input, String inputFormat){
		
		SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
		java.util.Date date = null;
		if(input != null){
			try {
				date = sdf.parse(input);
				return new java.sql.Date(date.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}else{
			return null;
		}

		
		
		
	}
	
	
//	public static String getFormatedDateString_(StringString inputFormat)
	
}
