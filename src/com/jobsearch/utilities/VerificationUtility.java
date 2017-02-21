package com.jobsearch.utilities;


public final class VerificationUtility {
	
	public static boolean isPositiveNumber(Integer number){
		
		if(number != null && number > 0) return true;
		else return false;
	}
	
	public static boolean isPositiveNumber(Double number){
		
		if(number != null && number > 0) return true;
		else return false;
	}
}
