package com.jobsearch.model

public class RateCriterion {
	Integer rateCriterionId
	String name
	Integer employeeId
	Integer jobId
	boolean isUsedToRateEmployee
	Double value
	
	public static int VALUE_NOT_YET_RATED = -1;
}
