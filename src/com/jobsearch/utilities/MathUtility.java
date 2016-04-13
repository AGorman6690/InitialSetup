package com.jobsearch.utilities;

public final class MathUtility {
	
	public static double round(double number, int decimalPlaces, int minRoundingValue){
		
		double multiplyer = Math.pow(10, decimalPlaces);		
		
		double value = number * multiplyer;
		value = Math.round(value);
		value = value / multiplyer;
		value = Math.max(value, minRoundingValue);
		return value;	
		
	}

}
