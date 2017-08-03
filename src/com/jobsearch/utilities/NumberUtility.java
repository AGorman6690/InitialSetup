package com.jobsearch.utilities;

public final class NumberUtility {
	public static boolean isPositiveNumber(Integer number){		
		if(number != null && number > 0) return true;
		else return false;
	}
	
	public static boolean isPositiveNumber(Double number){		
		if(number != null && number > 0) return true;
		else return false;
	}	
	
	public static boolean isPositiveNumber(String number) {		
		if(number != null){
			Double number_fromString = Double.valueOf(number);
			if(number_fromString == null || number_fromString <= 0) return false;
			else return true;
		}else return false;
	}

	public static boolean isPositiveNumberOrZero(Integer number){		
		if(number != null && number >= 0) return true;
		else return false;
	}
}
