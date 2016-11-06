package com.jobsearch.utilities;

public final class MathUtility {

	public static double round(double number, int decimalPlaces, int minRoundingValue) {

		double multiplier = Math.pow(10, decimalPlaces);

		double value = number * multiplier;
		value = Math.round(value) / multiplier;
		value = value / multiplier;
		value = Math.max(value, minRoundingValue);
		return value;
	}
}
