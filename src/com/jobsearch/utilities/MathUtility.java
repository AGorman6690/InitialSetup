package com.jobsearch.utilities;

public final class MathUtility {

	public static Double round(Double number, int decimalPlaces, int minRoundingValue) {

		if(number != null){
					
			double multiplier = Math.pow(10, decimalPlaces);
	
			double value = number * multiplier;
			value = Math.round(value);
			value = value / multiplier;
			value = Math.max(value, minRoundingValue);
			return value;
		}else return null;
	}
}
