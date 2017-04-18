package com.jobsearch.model

public class RateCriterion {
	Integer rateCriterionId
	String name
	Integer userId_ratee
	Integer userId_rater
	Integer jobId
	boolean isUsedToRateEmployee
	Double value
	String shortName
	
	String stringValue
	
	public static int VALUE_NOT_YET_RATED = -1;
}
