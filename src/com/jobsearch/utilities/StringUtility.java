package com.jobsearch.utilities;

public  final class StringUtility {
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}
	public static boolean isBlank(String str){
		if(str  == null || str.matches("")) return true;
		else return false;
	}
}
