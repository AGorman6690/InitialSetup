package com.jobsearch.model

import org.codehaus.jackson.annotate.JsonProperty

import com.google.gson.annotations.JsonAdapter

public class WorkDayProposal {
	
	@JsonProperty
	int id
	
	@JsonProperty
	int applicationId  
	
	@JsonProperty
	int workDayId
	
	@JsonProperty
	int status
	
	public static int STATUS_PROPOSED = 0;
	public static int STATUS_COUNTERED = 1;
	public static int STATUS_ACCEPTED = 2;
	public static int STATUS_DECLINED = 3;
}
